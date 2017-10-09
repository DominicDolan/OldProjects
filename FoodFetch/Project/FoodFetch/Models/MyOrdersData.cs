using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace FoodFetch.Models
{
    public class MyOrdersData
    {
        public int orderID { get; set; }
        public string restaurantName { get; set; }
        public decimal totalPrice { get; set; }
        public List<MyOrderDataItem> myOrderItems { get; set; }

        public class MyOrderDataItem
        {
            public int orderID;
            public string name { get; set; }
            public int quantity { get; set; }
            public decimal price { get; set; }
        }
    }
}