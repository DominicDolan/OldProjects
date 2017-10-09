using FoodFetch.Models;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace FoodFetch.Controllers
{
    public class UserController : Controller
    {

        public ActionResult Index()
        {
            return View("Register");
        }

        [HttpGet]
        public ActionResult Register()
        {
            return View();
        }

        [HttpPost]
        public ActionResult Register(UserModel user)
        {
            DAO dao = new DAO(HttpContext);
            int count = 0;
            ViewBag.RegisterPost = true;
            if (ModelState.IsValid)
            {
                count = dao.Insert(user);

                if (count == 1)
                {
                    Session["name"] = user.FirstName;
                    Session["email"] = user.Email;

                    if (Session["CartItems"] == null)
                        return RedirectToAction("Index", "Home");
                    else
                    {
                        var controller = DependencyResolver.Current.GetService<OrderController>();
                        controller.ControllerContext = new ControllerContext(Request.RequestContext, controller);
                        return controller.AddToCart(null, null);
                    }
                }

                else
                {
                    ViewBag.Status = "Error! " + " " + dao.message;
                }
                return View();
            }
            return View(user);
        }
        
        [HttpPost]
        public ActionResult Login(UserModel user)
        {
            DAO dao = new DAO(HttpContext);

            ViewBag.RegisterPost = false;
            string email = user.Email;
            ModelState.Remove("FirstName");
            ModelState.Remove("LastName");
            ModelState.Remove("ComparePassword");

            if (ModelState.IsValid)
            {
                user = dao.CheckLogin(user);
                if (user != null)
                {
                    Session["name"] = user.FirstName;
                    Session["email"] = user.Email;
                    ViewBag.Status = $"Email: {user.Email}, password: {user.Password}";

                    if (Session["CartItems"] == null)
                        return RedirectToAction("Index", "Home");
                    else
                    {
                        var controller = DependencyResolver.Current.GetService<OrderController>();
                        controller.ControllerContext = new ControllerContext(Request.RequestContext, controller);
                        return controller.AddToCart(null, null);
                    }

                }
                else
                {
                    ViewBag.Error = true;
                    return View("Register");
                }
            }
            return View("Register", user);
        }

        public ActionResult LogOut()
        {
            Session.Clear();
            Session.Abandon();
            return View();
        }
        
        private string ReadUserOrdersProcedure = "uspReadUserOrders";
        private string ReadOrderedItemsProcedure = "uspReadOrderedItems";

        private List<Order> ReadUserOrders(SqlCommand cmd)
        {
            cmd.Parameters.AddWithValue("@email", Session["email"].ToString());
            SqlDataReader reader = cmd.ExecuteReader();
            List<Order> orders = new List<Order>();
            while (reader.Read())
            {
                int id = int.Parse(reader["RestaurantId"].ToString());
                Order order = new Order()
                {
                    OrderId = int.Parse(reader["OrderId"].ToString()),
                    RestaurantId = int.Parse(reader["RestaurantId"].ToString()),
                    UserEmail = reader["UserEmail"].ToString(),
                    CustomerAddress = reader["CustomerAddress"].ToString()
                };
                orders.Add(order);
            }

            return orders;
        }

        private List<OrderedMenuItem> ReadOrderedItems(SqlCommand cmd)
        {
            SqlDataReader reader = cmd.ExecuteReader();
            var items = new List<OrderedMenuItem>();
            while (reader.Read())
            {
                OrderedMenuItem order = new OrderedMenuItem()
                {
                    OrderID = int.Parse(reader["OrderId"].ToString()),
                    MenuItemID = int.Parse(reader["MenuItemID"].ToString()),
                    Quantity = int.Parse(reader["Quantity"].ToString()),
                    Price = decimal.Parse(reader["Price"].ToString())
                };
                items.Add(order);
            }

            return items;
        }

        public ActionResult MyOrders()
        {
            if (Session["email"] == null)
                return RedirectToAction("Index", "Home");

            string message = "";
            var dao = new DAO(HttpContext);

            var orders = dao.ReadSQL(ReadUserOrdersProcedure, ReadUserOrders);
            
            var restaurantOrders = from order in orders
                                   join restaurant in dao.ReadList<Restaurant>() on order.RestaurantId equals restaurant.Id
                                   select new
                                   {
                                       restaurantName = restaurant.Name,
                                       orderId = order.OrderId
                                   };

            message += "Orders: " + orders.Count();
            message += ". Restaurant: " + restaurantOrders.Count();
            
            var orderedItems = from orderItem in dao.ReadSQL(ReadOrderedItemsProcedure, ReadOrderedItems)
                               join order in orders on orderItem.OrderID equals order.OrderId
                               join menuItem in dao.ReadList<MenuItem>() on orderItem.MenuItemID equals menuItem.Id
                               select new MyOrdersData.MyOrderDataItem()
                               {
                                   orderID = order.OrderId,
                                   name = menuItem.Name,
                                   quantity = orderItem.Quantity,
                                   price = orderItem.Price
                               };
            
            var orderDetails = from order in restaurantOrders
                               join item in orderedItems on order.orderId equals item.orderID into groupJoin
                               select new MyOrdersData()
                               {
                                   totalPrice = groupJoin.Sum((i) => i.price),
                                   orderID = order.orderId,
                                   restaurantName = order.restaurantName,
                                   myOrderItems = groupJoin.ToList()
                               };
            
            return View(orderDetails.ToList());
        }
    }
}