using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Helpers;
using System.Xml.Serialization;

namespace FoodFetch.Models
{
    public class DAO
    {

        public delegate T ReadOperation<T>(SqlCommand cmd);
        public static string InsertUserProcedure { get; } = "uspInsertUser";
        public static string CheckLoginProcedure { get; } = "uspCheckLogin";
        public static string InsertOrderedItemProcedure { get; } = "uspInsertOrderedItem";
        public static string InsertOrdersProcedure { get; } = "uspInsertOrder";

        SqlConnection conn;
        private string path;
        
        public string message { get; set; } = "";

        public DAO(string path)
        {
            this.path = path;
        }

        public DAO(HttpContextBase HttpContext)
        {
            path = HttpContext.Server.MapPath("~/App_Data/XML");
        }

        //Intialises a connection object
        public void Connection()
        {
            conn = new SqlConnection(@"Data Source=(LocalDB)\MSSQLLocalDB;AttachDbFilename=C:\Users\domin\Documents\Visual Studio 2017\Projects\FoodFetch\FoodFetch\App_Data\Data.mdf;Integrated Security=True");
        }

        public T ReadSQL<T>(string storedProc, ReadOperation<T> operation)
        {
            SqlCommand cmd;
            SqlDataReader reader;
            //Calling connection method to establish connection string
            Connection();
            cmd = new SqlCommand(storedProc, conn);
            cmd.CommandType = CommandType.StoredProcedure;
            T t = default(T);
            try
            {
                conn.Open();

                t = operation(cmd);
            }
            catch (SqlException ex)
            {
                message = ex.Message;
            }
            finally
            {
                conn.Close();
            }

            return t;
        }

        public int Insert(UserModel user)
        {

            //count shows the number of affected rows
            int count = 0;
            SqlCommand cmd;

            Connection();

            cmd = new SqlCommand(InsertUserProcedure, conn);
            cmd.CommandType = CommandType.StoredProcedure;

            cmd.Parameters.AddWithValue("@FirstName", user.FirstName);
            cmd.Parameters.AddWithValue("@LastName", user.LastName);
            cmd.Parameters.AddWithValue("@Email", user.Email);
            string password = Crypto.HashPassword(user.Password);
            cmd.Parameters.AddWithValue("@Password", password);
            
            try
            {
                conn.Open();
                count = cmd.ExecuteNonQuery();
            }
            catch (SqlException ex)
            {
                message = ex.Message;
            }
            finally
            {
                conn.Close();
            }

            return count;
        }

        public int Insert<T>(T t) where T : class
        {
            if (t.GetType() == typeof(UserModel))
            {
                UserModel user = t as UserModel;
                user.Password = Crypto.HashPassword(user.Password);
                user.ComparePassword = "";
            }
            var list = ReadList<T>();
            list.Add(t);

            Update(list);
            return 1;
        }

        //public UserModel CheckLogin(UserModel user)
        //{
        //    var users = ReadList<UserModel>();
        //    foreach (var userModel in users)
        //    {
        //        if (user.Email == userModel.Email &&
        //            Crypto.VerifyHashedPassword(userModel.Password, user.Password))
        //            return userModel;
        //    }
        //    return null;
        //}

        public UserModel CheckLogin(UserModel user)
        {
            UserModel LoginUser = null;
            SqlCommand cmd;
            SqlDataReader reader;
            string password;
            Connection();
            cmd = new SqlCommand(CheckLoginProcedure, conn);
            cmd.CommandType = CommandType.StoredProcedure;

            cmd.Parameters.AddWithValue("@email", user.Email);

            try
            {
                conn.Open();
                reader = cmd.ExecuteReader();

                LoginUser = ReadUser(reader);
                password = LoginUser.Password;
                int length = password.Length;
                if (!Crypto.VerifyHashedPassword(password, user.Password))
                {
                    LoginUser = null;
                }

                
            }
            catch (SqlException ex)
            {
                LoginUser = null;
                message = ex.Message;
            }
            catch (FormatException ex)
            {
                LoginUser = null;
                message = ex.Message;
            }
            finally
            {
                conn.Close();
            }

            return LoginUser;
        }

        public List<T> ReadList<T>() where T : class
        {
            XmlSerializer deserializer = new XmlSerializer(typeof(List<T>));
            using (TextReader reader = new StreamReader(path + "/" + typeof(T).Name + ".xml"))
            {
                try
                {
                    object obj = deserializer.Deserialize(reader);
                    var XmlData = obj;
                    return XmlData as List<T>;
                }
                catch
                {
                    return new List<T>();
                }
            }
        }


        public void Update<T>(List<T> list)
        {
            XmlSerializer serializer = new XmlSerializer(typeof(List<T>));
            using (TextWriter writer = new StreamWriter(path + "/" + typeof(T).Name + ".xml"))
            {
                serializer.Serialize(writer, list);
            }
        }

        public void InsertList<T>(List<T> list) where T:class
        {
            var currentList = ReadList<T>();
            currentList.AddRange(list);
            Update(currentList);
        }

        public UserModel ReadUser(SqlCommand cmd, string email)
        {
            cmd.Parameters.AddWithValue("@email", email.ToString());
            SqlDataReader reader = cmd.ExecuteReader();
            return ReadUser(reader);
        }

        public UserModel ReadUser(SqlCommand cmd)
        {
            SqlDataReader reader = cmd.ExecuteReader();
            return ReadUser(reader);
        }

        private UserModel ReadUser(SqlDataReader reader)
        {
            reader.Read();
            string address = reader["Address"] == null ? "" : reader["Address"].ToString();
            UserModel user = new UserModel()
            {
                FirstName = reader["FirstName"].ToString(),
                LastName = reader["LastName"].ToString(),
                Email = reader["Email"].ToString(),
                Password = reader["Password"].ToString(),
                Address = address
            };

            return user;
        }

        public int UpdateUser(UserModel user)
        {
            //count shows the number of affected rows
            int count = 0;
            SqlCommand cmd;

            Connection();

            cmd = new SqlCommand("uspUpdateUser", conn);
            cmd.CommandType = CommandType.StoredProcedure;

            cmd.Parameters.AddWithValue("@FirstName", user.FirstName);
            cmd.Parameters.AddWithValue("@LastName", user.LastName);
            cmd.Parameters.AddWithValue("@Email", user.Email);
            cmd.Parameters.AddWithValue("@Address", user.Address);

            try
            {
                conn.Open();
                count = cmd.ExecuteNonQuery();
            }
            catch (SqlException ex)
            {
                message = ex.Message;
            }
            finally
            {
                conn.Close();
            }

            return count;
        }

        public int InsertOrderedMenuItem(OrderedMenuItem item)
        {
            //count shows the number of affected rows
            int count = 0;
            SqlCommand cmd;

            Connection();

            cmd = new SqlCommand(InsertOrderedItemProcedure, conn);
            cmd.CommandType = CommandType.StoredProcedure;

            cmd.Parameters.AddWithValue("@OrderID", item.OrderID);
            cmd.Parameters.AddWithValue("@Quantity", item.Quantity);
            cmd.Parameters.AddWithValue("@MenuItemID", item.MenuItemID);
            cmd.Parameters.AddWithValue("@Price", item.Price);
            
            try
            {
                conn.Open();
                count = cmd.ExecuteNonQuery();
            }
            catch (SqlException ex)
            {
                message = ex.Message;
            }
            finally
            {
                conn.Close();
            }

            return count;
        }

        public void InsertOrderedMenuItemList(List<OrderedMenuItem> items)
        {
            foreach (var item in items)
            {
                InsertOrderedMenuItem(item);
            }
        }

        public int InsertOrder(Order order)
        {
            //count shows the number of affected rows
            int count = 0;
            SqlCommand cmd;

            Connection();

            cmd = new SqlCommand(InsertOrdersProcedure, conn);
            cmd.CommandType = CommandType.StoredProcedure;

            cmd.Parameters.AddWithValue("@OrderId", order.OrderId);
            cmd.Parameters.AddWithValue("@UserEmail", order.UserEmail);
            cmd.Parameters.AddWithValue("@RestaurantId", order.RestaurantId);
            cmd.Parameters.AddWithValue("@CustomerAddress", order.CustomerAddress);

            try
            {
                conn.Open();
                count = cmd.ExecuteNonQuery();
            }
            catch (SqlException ex)
            {
                message = ex.Message;
            }
            finally
            {
                conn.Close();
            }

            return count;
        }

        public void InsertOrderList(List<Order> items)
        {
            foreach (var item in items)
            {
                InsertOrder(item);
            }
        }
    }
}