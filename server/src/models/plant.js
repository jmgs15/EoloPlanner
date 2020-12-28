module.exports = (sequelize, DataTypes) => {
    const Plant = sequelize.define('plant', {
        id: {
            primaryKey: true,
            type: DataTypes.INTEGER,
            autoIncrement: true
        },
        city: {
            type: DataTypes.STRING,
            allowNull: false
        }
    });
    return Plant;
}