let express = require('express');
const router = express.Router();
const { findAllPlants, createPlant, deletePlant, getPlant } = require('../endpoints/plant');

router.get("/eolicplants/", findAllPlants);
router.get("/eolicplants/:id", getPlant);
router.post("/eolicplants/", createPlant);
router.delete("/eolicplants/:city", deletePlant);

module.exports = {
    plantRouter: router
}