using FoodFetch.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace FoodFetch.Controllers
{
    public class HomeController : Controller
    {

        // GET: Home
        public ActionResult Index()
        {
            var path = HttpContext.Server.MapPath("~/App_Data/XML");
            DAO dao = new DAO(path);
            return View(dao.ReadList<Restaurant>());
        }

        public ActionResult Contact()
        {
            return View();
        }

        public ActionResult About()
        {
            return View();
        }

        public ActionResult Privacy()
        {
            return View();
        }

        public ActionResult Sitemap()
        {
            return View();
        }

    }
}