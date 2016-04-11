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
        '?apikey=d0e7bf68cdda677938e6c186eaf2b755ef737cd8&outputMode=json&url=' + encodeURIComponent(searchTerm);
  //  http://gateway-a.watsonplatform.net/calls/url/URLGetText?apikey=d0e7bf68cdda677938e6c186eaf2b755ef737cd8&url=https://en.wikipedia.org/wiki/Portal:Alternative_rock
    var x = new XMLHttpRequest();
    x.open('GET', searchUrl);
    x.responseType = 'json';
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
function statusShow()
{
    document.getElementById("container1").style.display="none";
    document.getElementById("container2").style.display="none";
    document.getElementById("status").style.display="block";
}
function statusHide()
{
    document.getElementById("status").style.display="none";
    document.getElementById("container1").style.display="block";
    document.getElementById("container2").style.display="block";
}
function analyzeText(textData)
{
    var serverIp = "134.193.128.12";
    var textAnalysisUrl;

    if(textData!=null && textData!="" && textData.text!=null && textData.text!="")
    {
        textAnalysisUrl = "http://localhost:12344/DocumenticsService/analysis?ip=" + serverIp + "&documentText=" +textData.text.toString();
        var x = new XMLHttpRequest();
        x.open('GET', textAnalysisUrl);
        x.responseType = 'json';
        x.onload = function() {
            var analysisData = x.response;
            if (!analysisData ) {
              //  errorCallback('No response from API!');
                alert("No response from the service. Please try again later!")
                return;
            }
           showData(analysisData);
        };
        x.onerror = function() {
            //errorCallback('Network error.');
            alert("No response from the service. Please try again later!")
        };
        x.send();
    }
}
function showData(analysisResult) {
    if (analysisResult != null && analysisResult != "" && analysisResult.analysis!=null) {
    var topWOrds = analysisResult.analysis.tfidf;
        statusHide();
        if(topWOrds!=null && topWOrds.length>0)
        {
            createWordCloud(topWOrds,"tfIdfResult");

            //alert("The document has been analyzed");
        }
        var dateEntities = analysisResult.analysis.dateEntities;
        var nameEntities = analysisResult.analysis.nameEntities;
        var orgEntities = analysisResult.analysis.organisationEntities;
        var impDates = new Array();
        var impNames = new Array();
        var impOrganizations = new Array();
        if(dateEntities!=null && dateEntities.length>0)
        {
            for(var i=0;i<dateEntities.length;i++)
            {

                if(dateEntities[i].split(",").length>1 && dateEntities[i].split(",")[0].split(" ").length>1)
                {
                    impDates[impDates.length] =dateEntities[i];

                }
            }
            createListOfEntities(impDates,"dates");

           // createWordCloud(impDates,"entities");
        }
        if(nameEntities!=null && nameEntities.length>0)
        {
            for(var i=0;i<nameEntities.length;i++)
            {

                if(nameEntities[i]!=null && nameEntities[i]!="")
                {
                    impNames[impNames.length] =nameEntities[i];
                }
            }
            createListOfEntities(impNames,"names");

            // createWordCloud(impDates,"entities");
        }
        if(orgEntities!=null && orgEntities.length>0)
        {
            for(var i=0;i<orgEntities.length;i++)
            {

                if(orgEntities[i]!= null && orgEntities[i]!="")
                {
                    impOrganizations[impOrganizations.length] =orgEntities[i];
                }
            }
            createListOfEntities(impOrganizations,"organizations");

            // createWordCloud(impDates,"entities");
        }

    }

}
function createListOfEntities(entityArray, divId)
{
    if(entityArray!=null && entityArray.length>0)
    {
        var htmlString ="";
        var cnt =1;
        for(var i=0;i<entityArray.length;i++)
        {
            htmlString+='<p>' + cnt++ + '. '+ entityArray[i] + '</p>'
        }
        var element = document.getElementById(divId);
        element.innerHTML +=htmlString;
    }
}
function createWordCloud(wordArray, divId)
{
    var wordArray = wordArray;
    var fill = d3.scale.category20();

    d3.layout.cloud().size([300, 300])
        .words(wordArray.map(function(d) {
            return {text: d.word,  size: 10 + Math.random() * 50};
        }))
        .rotate(function() { return ~~(Math.random() * 2) * 90; })
        .font("Impact")
        .fontSize(function(d) { return d.size; })
        .on("end", draw)
        .start();

    function draw(words) {
        d3.select("#"+divId).append("svg")
            .attr("width", 300)
            .attr("height", 300)
            .append("g")
            .attr("transform", "translate(150,150)")
            .selectAll("text")
            .data(words)
            .enter().append("text")
            .style("font-size", function(d) { return d.size + "px"; })
            .style("font-family", "Impact")
            .style("fill", function(d, i) { return fill(i); })
            .attr("text-anchor", "middle")
            .attr("transform", function(d) {
                return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
            })
            .text(function(d) { return d.text; });
    }
}
document.addEventListener('DOMContentLoaded', function() {

    statusShow();
    getCurrentTabUrl(function(url) {

        callText(url, function(response) {
          //  renderStatus('success' );
          //  var Result = document.getElementById('result');
           // Result.innerHTML=response;
           // statusShow();
            analyzeText(response);

        }, function(errorMessage) {
            statusHide();
            alert("No response from the service. Please try again later!");
        });
    });
});