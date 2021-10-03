var btn_audio = document.querySelector("#btn-audio"),
    btn_video = document.querySelector("#btn-video"),
    btn_sharing = document.querySelector("#btn-sharing");

var div_audioFig = document.querySelector("#audiofigure"),
    div_videoFig = document.querySelector("#videofigure"),
    div_sharingFig = document.querySelector("#sharingfigure");
var audioJson, videoJson, sharingJson, statusJson,deviceJson, signalJson, logTime;
var analyseKey = ["pdrReceived", "avgJitterDelay", "bitrateReceived", "bitrateSent",
    "jitterReceived", "jitterSent", "packetsReceived", "packetsSent", "rtt", "remotePdr","fHeightReceived","fHeightSent"];

function parseFormatedData(formatdata) {
    var json;
    var arr = formatdata.split("\r\n");
    if (arr.length > 0) {
        for (var i = 0; i < arr.length; i++) {
            var data = arr[i];
            if(data == "")continue;
            json = JSON.parse(data);
            var mediatype = json["mediatype"];
            if (mediatype == "audio") {
                audioJson = json;
            }
            else if (mediatype == "video") {
                videoJson = json;
            }
            else if (mediatype == "sharing") {
                sharingJson = json;
            }
            else if(mediatype == "mediastatus"){
                statusJson = json;
            }
            else if(mediatype == "devicechange"){
                deviceJson = json;
            }
            else if(mediatype == "signalstatus"){
                signalJson = json;
            }
        }
        renderFigure(audioJson);
    }
}

function renderMediaStatus(mediatype){
    var formattime = logTime.split(" ")[0];
    if(formattime == ""){
        formattime = [new Date().getFullYear(),new Date().getMonth() + 1,new Date().getDay()].join("-");
    }
    var json = statusJson[mediatype];
    var serieslist = [],legenddata = [];
    var time = json["time"];
    for (var i = 0; i < 11; i++) {
        if (json[i] && json[i].length > 0) {
            var serdata = [];
            for(var j = 0; j < json[i].length; j++){
                var peerdata = [formattime + " " + time[j],json[i][j]];
                serdata.push(peerdata);
            }
            var series ={
                name: 'peer' + i,
                type: 'line',
                symbol: 'none',
                data: serdata
            }
            legenddata.push('peer' + i);
            serieslist.push(series);
        }
    } 
    if(serieslist.length <= 0){
        console.log("no mediastate data accessed.");
        return false;
    }
    var xAxis = {type: 'time',boundaryGap: false};
    var yAxis = {type: 'value',minInterval: 1,
        axisLabel: {
            formatter:(function(value) {
                var texts = [];
                if (value <= -2) {
                    texts.push('failed');
                }
                else if (value <= -1) {
                    texts.push('disconnected');
                }
                else if (value <= 0) {
                    texts.push('checking');
                }
                else if (value <= 1) {
                    texts.push('connected');
                }
                else {
                    texts.push('closed');
                }
                return texts;
            })
        }
    };
    renderChart('mediaStatus', xAxis, yAxis, legenddata, serieslist, 'mediaStatus' + mediatype);
    return true;
}

function renderSignalStatus(mediatype){
    var formattime = logTime.split(" ")[0];
    if(formattime == ""){
        formattime = [new Date().getFullYear(),new Date().getMonth() + 1,new Date().getDay()].join("-");
    }
    var json = signalJson[mediatype];
    var serieslist = [],legenddata = [];
    var time = json["time"];
    for (var i = 0; i < 11; i++) {
        if (json[i] && json[i].length > 0) {
            var serdata = [];
            for(var j = 0; j < json[i].length; j++){
                var peerdata = [formattime + " " + time[j],json[i][j]];
                serdata.push(peerdata);
            }
            var series ={
                name: 'peer' + i,
                type: 'line',
                symbol: 'none',
                data: serdata
            }
            legenddata.push('peer' + i);
            serieslist.push(series);
        }
    } 
    if(serieslist.length <= 0){
        console.log("no signalstate data accessed.");
        return false;
    }
    var xAxis = {type: 'time',boundaryGap: false};
    var yAxis = {type: 'value',minInterval: 1,
        axisLabel: {
            formatter:(function(value) {
                var texts = [];
                if (value <= -3) {
                    texts.push('have-remote-pranswer');
                }
                else if (value <= -2) {
                    texts.push('have-local-pranswer');
                }
                else if (value <= -1) {
                    texts.push('have-remote-offer');
                }
                else if (value <= 0) {
                    texts.push('have-local-offer');
                }
                else if(value <= 1){
                    texts.push('stable');
                }
                else{
                    texts.push('closed');
                }
                return texts;
            })
        }
    };
    renderChart('signalStatus', xAxis, yAxis, legenddata, serieslist, 'signalStatus' + mediatype);
    return true;
}

function renderDeviceChange(){
    if(deviceJson == undefined || deviceJson == ""){
        console.log("renderDeviceChange illegal data:" + json);
        return;
    }
    var formattime = logTime.split(" ")[0];
    if(formattime == ""){
        formattime = [new Date().getFullYear(),new Date().getMonth() + 1,new Date().getDay()].join("-");
    }
    var serieslist = [],legenddata = [];
    for (key in deviceJson) {
        if(key == "mediatype")continue;
        if (deviceJson[key]["time"].length > 0) {
            var serdata = [];
            for(var i = 0; i < deviceJson[key]["time"].length; i++){
                var peerdata = [formattime + " " + deviceJson[key]["time"][i],deviceJson[key]["type"][i], deviceJson[key]["devices"][i]];
                serdata.push(peerdata);
            }
            var series ={
                name: key,
                type: 'line',
                data: serdata
            }
            legenddata.push(key);
            serieslist.push(series);
        }
    } 
    if(serieslist.length <= 0){
        console.log("no devicestatus data accessed.");
        return false;
    }
    var xAxis = {type: 'time', boundaryGap: false};
    var yAxis = {type: 'value', minInterval: 1,
        axisLabel: {
            formatter: (function(value){
                var texts = [];
                if (value <= -1) {
                    texts.push('remove');
                }
                else if (value <= 0) {
                    texts.push('init');
                }
                else if (value <= 1) {
                    texts.push('add');
                }                   
                else {
                    texts.push('init');
                }
                return texts;
            })
        }
    };
    var tooltip = {trigger: 'axis',axisPointer: {type: 'shadow' },
        formatter:(function(parmas){
            if(parmas.length < 0)return;
            var tips = ['time:' + parmas[0].axisValueLabel];
            for(var i = 0; i < parmas.length; i++){
                var tip = parmas[i].seriesName +":";
                var devicearr = parmas[i].value[2];
                for(var j in devicearr){
                    tip += devicearr[j].label + '<br/>' + "        ";
                }
                tips.push(tip);
            }
            return tips.join('<br/>');
        })
    };
    renderChart('deviceChange', xAxis, yAxis, legenddata, serieslist, 'deviceChange', tooltip);
    return true;
}

function renderFigure(json) {
    if(json == undefined || json == "")
    {
        console.log("illegal data:" + json);
        return;
    }
    var bRender = false;
    div_audioFig.style.display="none";
    div_videoFig.style.display="none";
    div_sharingFig.style.display="none";
    if (json["mediatype"] == "audio") {
        div_audioFig.style.display="";
        renderDeviceChange();
    }
    else if (json["mediatype"] == "video") {
        div_videoFig.style.display="";
    }
    else if (json["mediatype"] == "sharing") {
        div_sharingFig.style.display="";
    }

    /*render*/
    var formattime = logTime.split(" ")[0];
    if(formattime == ""){
        formattime = [new Date().getFullYear(),new Date().getMonth() + 1,new Date().getDay()].join("-");
    }
    bRender = renderMediaStatus(json["mediatype"]);
    bRender = renderSignalStatus(json["mediatype"]) || bRender;
    for (var i = 0; i < analyseKey.length; i++) {
        var key = analyseKey[i];
        var serieslist = [], legenddata = [];
        var time = json[key]["time"];
        if(json[key]["common"] && json[key]["common"].length > 0){//add common series
            var serdata = [];
            for(var k = 0; k < json[key]["common"].length; k++){
                var peerdata = [formattime + " " + time[k],json[key]["common"][k]];
                serdata.push(peerdata);
            }
            var commonserie = {
                name: key+'-common',
                type: 'line',
                symbol: 'none',
                data: serdata
            };
            legenddata.push(key + '-common');
            serieslist.push(commonserie);
        }
       
        for (var j = 0; j < 11; j++) {//add peer series
            var flagkey = "render_peer" + j;
            if (json[key][flagkey] == "true") {
                var serdata = [];
                for (var k = 0; k < json[key][j].length; k++) {
                    var peerdata = [formattime + " " + time[k], json[key][j][k]];
                    serdata.push(peerdata);
                }
                var serie = {
                    name: key+'-peer' + j,
                    type: 'line',
                    symbol: 'none',
                    data: serdata
                };
                legenddata.push(key + '-peer' + j);
                serieslist.push(serie);
            }
        }
        if(serieslist.length == 0 ){
            console.log(key + " has no data");
            continue;
        }
        bRender = true;
        var xAxis = {type: 'time',boundaryGap: false};
        var yAxis = {type: 'value'};
        renderChart(key,xAxis,yAxis,legenddata,serieslist,key + json["mediatype"]);
    }
    if(!bRender)alert("No Data Found!");
}

function renderChart(title, xAxis, yAxis, legendData, seriesList, domId, toolTip){
    var dom = document.getElementById(domId);
        if(!dom){
            console.log("dom null,id:", domId);
            return;
        }
    var myChart = echarts.init(dom);
    var option = {
        title: {text: title},
        tooltip: toolTip || {trigger: 'axis',axisPointer: {type: 'shadow' }},
        toolbox: {
            feature: {
                dataZoom: {yAxisIndex: 'none'},
                dataView: {readOnly: true},
                restore: {},
                saveAsImage: {}
            }
        },
        legend: {data: legendData},
        xAxis: xAxis,
        yAxis: yAxis,
        dataZoom: [{type: 'inside',start: 0,end: 100}, {start: 0,end: 100}],
        series: seriesList
    }
    if (option && typeof option === 'object') {
        myChart.setOption(option);
    }
}

document.onreadystatechange = function (e) {
    if (document.readyState == 'complete') {
        var id = "";
        var addrurl = window.location.search.substring(1);
        var index = addrurl.indexOf("id=");
        if (index > -1) {
            id = addrurl.substring(3, addrurl.length);
        }
        console.log("addrurl=" + addrurl);
        console.log("addridurl=" + id);

        $.ajax({
            type: 'GET',
            url: "/pan/tool/api/log/" + id,
            success: function (result) {
                console.log(result.data.formatted_data);
                logTime = result.data.date_time;
                parseFormatedData(result.data.formatted_data);
            },
            error: function (result) {
                alert("Row deleted failed ")
            }
        });
    }
}
btn_audio.addEventListener("click", function(){
    btn_audio.setAttribute("class","el-tabs__item is-top is-active");
    btn_video.setAttribute("class","el-tabs__item is-top");
    btn_sharing.setAttribute("class","el-tabs__item is-top");
    renderFigure(audioJson)
});
btn_video.addEventListener("click", function(){
    btn_audio.setAttribute("class","el-tabs__item is-top");
    btn_video.setAttribute("class","el-tabs__item is-top is-active");
    btn_sharing.setAttribute("class","el-tabs__item is-top");
    renderFigure(videoJson)
});
btn_sharing.addEventListener("click", function(){
    btn_audio.setAttribute("class","el-tabs__item is-top");
    btn_video.setAttribute("class","el-tabs__item is-top");
    btn_sharing.setAttribute("class","el-tabs__item is-top is-active");
    renderFigure(sharingJson)
});