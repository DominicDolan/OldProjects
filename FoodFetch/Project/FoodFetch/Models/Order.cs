using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace FoodFetch.Models
{
    public class Order
    {
        public int OrderId { get; set; }
        public int RestaurantId { get; set; }
        public string UserEmail { get; set; }
        public string CustomerAddress { get; set; }
    }
}