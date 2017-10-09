using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace FoodFetch.Models
{
    public class Restaurant
    {
        public int Id { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
        public string Address { get; set; }
        public string ImageURL { get; set; }
        public byte Stars { get; set; }

        public Restaurant()
        {

        }

        public Restaurant(int Id)
        {
            this.Id = Id;
        }
        
    }
}