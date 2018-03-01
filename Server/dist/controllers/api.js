"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
const graph = require("fbgraph");
const multer = require("multer");
const upload = multer();
/**
 * GET /api
 * List of API examples.
 */
exports.getApi = (req, res) => {
    res.render("api/index", {
        title: "API Examples"
    });
};
/**
 * GET /api/facebook
 * Facebook API example.
 */
exports.getFacebook = (req, res, next) => {
    const token = req.user.tokens.find((token) => token.kind === "facebook");
    graph.setAccessToken(token.accessToken);
    graph.get(`${req.user.facebook}?fields=id,name,email,first_name,last_name,gender,link,locale,timezone`, (err, results) => {
        if (err) {
            return next(err);
        }
        res.render("api/facebook", {
            title: "Facebook API",
            profile: results
        });
    });
};
exports.postProcessAudio = (req, res, next) => {
    upload.single("audio_sample")(req, res, (err) => {
        if (err) {
            res.json({ "status": -1, "errors": err });
        }
        console.log(req.body);
        console.log(req.file);
        console.log(req.files);
        res.json({ "status": 1 });
    });
};
//# sourceMappingURL=api.js.map