module.exports = (sequelize, DataTypes) => {
    const Plant = sequelize.define('plant', {
        city: {
            primaryKey: true,
            type: DataTypes.STRING
        },
        progress: {
            allowNull: false,
            type: DataTypes.INTEGER
        }
    });
    return Plant;
}