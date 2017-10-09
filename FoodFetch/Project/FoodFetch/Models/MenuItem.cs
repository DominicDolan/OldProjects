using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace FoodFetch.Models
{
    public class MenuItem
    {
        public int Id { get; set; }
        public int RestaurantId { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public decimal Price { get; set; }

        public MenuItem()
        {

        }

        public MenuItem(int id)
        {
            Id = id;
        }
    }
}