module.exports = (app, Plant) => {
    app.get("/eolicplants/", (req, res) =>
        Plant.findAll().then((result) => res.json(result))
    );

    app.post("/eolicplants/", (req, res) =>
        Plant.create({
            city: req.body.city,
            progress: req.body.progress
        }).then((result) => res.json(result))
    );

    app.delete("/eolicplants/:city", (req, res) =>
        Plant.destroy({
            where: {
                city: req.params.city
            }
        }).then((result) => res.json(result))
    );
}