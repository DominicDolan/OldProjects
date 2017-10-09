using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.ComponentModel.DataAnnotations;
using System.Data.SqlClient;
using System.Data;
using System.Web.Helpers;

namespace FoodFetch.Models
{
    public class UserModel
    {
        [Display(Name = "First Name")]
        [Required]
        [RegularExpression("[A-Za-z]+")]
        public string FirstName { get; set; }

        [Display(Name = "Last Name")]
        [Required]
        [RegularExpression("[A-Za-z]+")]
        public string LastName { get; set; }

        [Required]
        //[DataType(DataType.EmailAddress)]
        [EmailAddress]
        public string Email { get; set; }

        [Required]
        //[DataType(DataType.Password)]
        [StringLength(10, ErrorMessage =
            "Password must be 5 to 10 characters long",
            MinimumLength = 5)]
        public string Password { get; set; }

        [Display(Name = "Confirm Password")]
        [Required]
        //[DataType(DataType.Password)]
        [StringLength(10, ErrorMessage =
            "Password must be 5 to 10 characters long",
            MinimumLength = 5)]
        [Compare("Password")]
        public string ComparePassword { get; set; }

        public string Address { get; set; }

        public override string ToString()
        {
            return $"{FirstName} {LastName}, {Email}, Password: {Password}";
        }
        
    }
}