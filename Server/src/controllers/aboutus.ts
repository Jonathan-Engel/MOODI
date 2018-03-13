import { Request, Response } from "express";

/**
 * GET /
 * About Us page.
 */
export let index = (req: Request, res: Response) => {
  res.render("aboutus", {
    title: "Aboutus"
  });
};