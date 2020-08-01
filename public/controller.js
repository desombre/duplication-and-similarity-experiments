window.onload = (event) => {
    console.debug("onload");
    getAlgorithms((response) => {
        let select = byId("algorithmSelect");
        for (let algorithm of JSON.parse(response)) {
            let option = document.createElement("option");
            option.text = algorithm;
            option.value = algorithm;
            select.add(option);
        }

    });
};

const BASE_URL = "http://" + window.location.host + "/api";
const RESULT_PREFIX = "";

const byId = (id) => {
    return document.getElementById(id);
};

const asyncRequest = (method, endpoint, callback, body) => {
    let xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.readyState === 4) {
            if (xmlHttp.status === 200 || xmlHttp.status === 202) {
                callback(xmlHttp.response);
            } else {
                console.error(xmlHttp.response);
            }
        }
    };

    xmlHttp.open(method, (BASE_URL + endpoint), true);
    xmlHttp.send(body);
};

const generateEndpointWithParameters = (endpoint, parameterMap) => {
    let newEndpointString = endpoint + "?";
    for (const [key, value] of Object.entries(parameterMap)) {
        newEndpointString += `${key}=${value}&`;
    }
    return newEndpointString;
};

const getAlgorithms = (callback) => {
    asyncRequest("GET", "/allAlgorithms", callback, null);
};


const calculate = (callback) => {
    asyncRequest("GET",
        generateEndpointWithParameters("/calculate", {
            "algorithm": byId("algorithmSelect").value,
            "s1": byId("s1").value,
            "s2": byId("s2").value
        }),
        callback,
        null);
};

const onCalculateClick = () => {
    calculate((response) => {
        byId("results").appendChild(document.createTextNode(RESULT_PREFIX + response));
        byId("results").appendChild(document.createElement("br"));
    });
};

const onDetectDuplicatesClick = () => {
    asyncRequest("GET",
        generateEndpointWithParameters("/duplication", {
            "s1": byId("s1").value,
            "s2": byId("s2").value
        }),
        showDuplicationResult,
        null);
}

const showDuplicationResult = (response) => {
    for (let detectedDuplicate of JSON.parse(response)) {
        let formattedText = highlight(detectedDuplicate.i2.description, detectedDuplicate.startDuplicate, detectedDuplicate.length);
        let div = document.createElement('div');
        div.innerHTML = formattedText.trim();
        byId("results").appendChild(div);
        byId("results").appendChild(document.createElement("hr"));
    }
}
const onResetResultsClick = () => {
    byId("results").innerHTML = "Result: <br/>";
}
const highlight = (text, start, length) => {
    let textBufferSize = 12;
    let end = start + length;
    let dots = "...";
    let prefix = start === 0 ? "" : dots;
    let suffix = end === text.length ? "" : dots;

    return prefix + text.substring(Math.max(0, start-textBufferSize), start) + "<span class='highlight'>" + text.substring(start, end) + "</span>" + text.substring(end, Math.min(end + textBufferSize, text.length)) + suffix;
}