function weather(call, callback) {
    console.log("Request received:" + JSON.stringify(call));

    var {city} = call.request;

    const vowels = "aeiou";
    let firstChar = city.trim().charAt(0).toLowerCase();
    let weather = vowels.indexOf(firstChar) !== -1 ? "sunny" : "rainy";

    let waitRandomMilliSeconds = (Math.floor(Math.random() * 3) + 1) * 1000;

    setTimeout(function () {
        callback(null, {weather: weather});
    }, waitRandomMilliSeconds);
}

exports.weather = weather;