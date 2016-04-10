function getCurrentTabUrl(callback) {
    var queryInfo = {
        active: true,
        currentWindow: true
    };

    chrome.tabs.query(queryInfo, function(tabs) {
        //console.log('tabs: ' + tabs);
        var tab = tabs[0];
        console.log('tab: ' + tabs[0]);
        var url = tab.url;
        console.log('url: ' + url);
       // alert(url);
        console.assert(typeof url == 'string', 'tab.url should be a string');
        callback(url);
    });
}

function callText(searchTerm,callback, errorCallback) {
    var searchUrl = 'http://gateway-a.watsonplatform.net/calls/url/URLGetText' +
        '?apikey=d0e7bf68cdda677938e6c186eaf2b755ef737cd8&url=' + encodeURIComponent(searchTerm);
  //  http://gateway-a.watsonplatform.net/calls/url/URLGetText?apikey=d0e7bf68cdda677938e6c186eaf2b755ef737cd8&url=https://en.wikipedia.org/wiki/Portal:Alternative_rock
    var x = new XMLHttpRequest();
    x.open('GET', searchUrl);
    x.responseType = 'xml';
    x.onload = function() {
        var response = x.response;
        if (!response ) {
            errorCallback('No response from API!');
            return;
        }
        callback(response);
    };
    x.onerror = function() {
        errorCallback('Network error.');
    };
    x.send();
}

function renderStatus(statusText) {
    document.getElementById('status').textContent = statusText;
}

document.addEventListener('DOMContentLoaded', function() {
    getCurrentTabUrl(function(url) {
        renderStatus('url is' + url);
        callText(url, function(response) {


            renderStatus('success' );
            var Result = document.getElementById('result');
            Result.innerHTML=response;
        }, function(errorMessage) {
            renderStatus('Failed ' + errorMessage);
        });
    });
});