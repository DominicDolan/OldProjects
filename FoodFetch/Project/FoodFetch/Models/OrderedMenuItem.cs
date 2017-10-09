using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace FoodFetch.Models
{
    public class OrderedMenuItem
    {
        public int Quantity { get; set; }
        public int OrderID { get; set; }
        public int MenuItemID { get; set; }
        public decimal Price { get; set; }
    }
}