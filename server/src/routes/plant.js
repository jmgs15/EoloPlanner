module.exports = (app, Plant, queue) => {

    app.get("/eolicplants/", (req, res) =>
        Plant.findAll().then((result) => res.json(result))
    );

    app.post("/eolicplants/", (req, res) =>
        Plant.create({
            city: req.body.city
        }).then((result) => {
            res.json({
                id: result.id,
                city: result.city,
                progress: 0,
                completed: false,
                planning: null
            });
            queue.sendMessage(JSON.stringify({id: result.id, city: result.city}));
        }).catch(function (err) {
            console.log(err);
            //TODO: Cambiar el error
            res.json(err);
        })
    );

    app.delete("/eolicplants/:city", (req, res) =>
        Plant.destroy({
            where: {
                city: req.params.city
            }
        }).then((result) => res.json(result))
    );

    app.get("/eolicplants/:id", (req, res) =>
        Plant.findOne({
            where: {
                id: req.params.id
            }
        }).then(function (result) {
            if (!result) {
                res.status(404).send("Not found eolicplant at id:" + req.params.id);
            } else {
                res.json(result).status(200);
            }
        }));
}