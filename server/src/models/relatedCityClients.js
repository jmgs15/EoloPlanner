var RealatedCityClients = module.exports = {
    array: [],
    add: function(city, client) {
        RealatedCityClients.array.push({city: city, client: client});
    },
    remove: function(city) {
        //TODO: Ver como eliminar del array
        RealatedCityClients.array = RealatedCityClients.array.filter(item => item.city !== city);
        for( var i = 0; i < RealatedCityClients.array.length; i++){
            if ( RealatedCityClients.array[i].city === city) {
                RealatedCityClients.array.splice(i, 1);
            }
        }
    },
    getClient: function(city) {
        return RealatedCityClients.array.filter(item => item.city === city)[0];
    }
}