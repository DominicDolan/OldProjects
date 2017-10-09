using FoodFetch.Models;
using System;
using System.Collections.Generic;
using System.Data.SqlClient;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace FoodFetch.Controllers
{
    public class OrderController : Controller
    {
        private int RestaurantID;

        // GET: Order
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult Restaurant(int id)
        {
            DAO dao = new DAO(HttpContext);
            ViewBag.RestaurantID = id;
            ViewBag.Title =(from restaurant in dao.ReadList<Restaurant>()
                            where restaurant.Id == id
                            select restaurant.Name).Single();

            var RestaurantMenu =(from item in dao.ReadList<MenuItem>()
                                 where item.RestaurantId == id
                                 select item).ToList();

            return View(RestaurantMenu);
        }

        //public ActionResult AddToCart(int restaurantID, int menuItemID)
        //{
        //    //TODO: Add item to cart table


        //    DAO dao = new DAO();

        //    ViewBag.RestaurantID = restaurantID;
        //    ViewBag.Title = dao.Restaurants[restaurantID].Name;

        //    foreach (var menuItem in dao.MenuItems)
        //    {
        //        if (menuItem.Id == menuItemID)
        //        {
        //            ViewBag.menuItem = menuItem;
        //        }
        //    }

        //    return View("Restaurant", GetRestaurantMenu(restaurantID));
        //}

        [HttpPost]
        public ActionResult AddToCart(int[] quantity, int[] MenuItemID)
        {
            DAO dao = new DAO(HttpContext);
            var MenuItems = dao.ReadList<MenuItem>();

            var Names = new Dictionary<int, string>();
            List<OrderedMenuItem> CartItems = new List<OrderedMenuItem>();
            decimal totalPrice = 0;

            if (Session["CartItems"] == null)
            {
                for (int i = 0; i < quantity.Length; i++)
                {
                    if (quantity[i] != 0)
                    {
                        MenuItem chosenItem = MenuItems.Find(m => MenuItemID[i] == m.Id);
                        decimal price = quantity[i] * chosenItem.Price;
                        totalPrice += price;
                        Names.Add(chosenItem.Id, chosenItem.Name);

                        CartItems.Add(new OrderedMenuItem()
                        {
                            MenuItemID = chosenItem.Id,
                            Quantity = quantity[i],
                            Price = price
                        });
                    }
                }

                Session["CartItems"] = CartItems;
            }
            else
            {
                CartItems = (List<OrderedMenuItem>)Session["CartItems"];
                foreach (var item in CartItems)
                {
                    MenuItem chosenItem = MenuItems.Find(m => item.MenuItemID == m.Id);
                    Names.Add(chosenItem.Id, chosenItem.Name);
                    totalPrice += item.Price;
                }
            }


            if (Session["email"] != null) {
                var user = dao.ReadSQL("uspCheckLogin", (cmd) => 
                        dao.ReadUser(cmd, Session["email"].ToString())
                    );
                
                if (user.Address != null)
                    ViewBag.Address = user.Address.Split(',');
            }

            ViewBag.Names = Names;
            ViewBag.TotalPrice = totalPrice;
            
            return View("../Order/PlaceOrder");
        }

        private string ReadUserOrdersProcedure = "uspReadUserOrders";

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

        [HttpPost]
        public ActionResult OrderPlaced(string street1, string street2, string City, string County, string PostCode)
        {
            string email;
            if (Session["email"] == null)
            {
                return RedirectToAction("Register", "User");
            }
            email = (string)Session["email"];

            string address = $"{street1}, {street2}, {City}, {County}, {PostCode}";

            DAO dao = new DAO(HttpContext);

            var user = dao.ReadSQL("uspCheckLogin", (cmd) =>
                        dao.ReadUser(cmd, Session["email"].ToString())
                    );


            //var user =(from userModel in users
            //           where userModel.Email == (string)Session["email"]
            //           select userModel).Single();
            user.Address = address;

            var orders = dao.ReadSQL(ReadUserOrdersProcedure, ReadUserOrders);
            int newId = 0;
            if (orders.Count > 0)
                newId = orders.Last().OrderId + 1;

            var CartItems = (List<OrderedMenuItem>)Session["CartItems"];

            int restaurantId = (from item in dao.ReadList<MenuItem>()
                                where item.Id == CartItems[0].MenuItemID
                                select item.RestaurantId).First();

            Order order = new Order()
            {
                OrderId = newId,
                RestaurantId = restaurantId,
                UserEmail = email,
                CustomerAddress = address
            };

            foreach (var item in CartItems)
            {
                item.OrderID = newId;
            }

            dao.InsertOrderedMenuItemList(CartItems);
            dao.InsertOrder(order);
            dao.UpdateUser(user);

            Session["CartItems"] = null;

            return View();
        }

        public ActionResult PlaceOrder()
        {
            return RedirectToAction("Index", "Home");
        }
        
    }
}