import * as nodemailer from "nodemailer";
import { Request, Response } from "express";

const transporter = nodemailer.createTransport({
  service: "SendGrid",
  auth: {
    user: "moodi123",
    pass: "moodi1234"
  }
});

/**
 * GET /contact
 * Contact form page.
 */
export let getContact = (req: Request, res: Response) => {
  res.render("contact", {
    title: "Contact"
  });
};

/**
 * POST /contact
 * Send a contact form via Nodemailer.
 */
export let postContact = (req: Request, res: Response) => {
  req.assert("name", "Name cannot be blank").notEmpty();
  req.assert("email", "Email is not valid").isEmail();
  req.assert("message", "Message cannot be blank").notEmpty();

  const errors = req.validationErrors();

  if (errors) {
    req.flash("errors", errors);
    return res.redirect("/contact");
  }

const sgMail = require("@sendgrid/mail");
sgMail.setApiKey("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");
const msg = {
    to: "shreyajacob1995@gmail.com",
    from: `${req.body.name} <${req.body.email}>`,
    subject: "Contact Form",
    text: req.body.message
  };
  sgMail.send(msg);

  transporter.sendMail(sgMail, (err) => {
    if (err) {
      req.flash("errors", { msg: err.message });
      return res.redirect("/contact");
    }
    req.flash("success", { msg: "Email has been sent successfully!" });
    res.redirect("/contact");
  });
};


