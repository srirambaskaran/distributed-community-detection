function movieGenreDistribution() {
	$.get("moviegenredistribution", function(dataStr){
		var chart = getHighchartsJSONObject("bar");
		var data = JSON.parse(dataStr);
		
		chart.xAxis.categories = data.categories;
		chart.series.push( data.series);
		chart.legend = {}
		chart.legend.enabled = false;
		chart.title.text="Distribution of Genres";
		Highcharts.chart("userMovieDistribution", chart);
	});
}

function getCommunityDistribution() {
	$.get("communitydistribution", function(dataStr) {
		var chart = getHighchartsJSONObject("column");
		var data = JSON.parse(dataStr);
		
		chart.xAxis.categories = data.categories;
		chart.series.push( data.series);
		chart.legend = {}
		chart.legend.enabled = false;
		chart.title.text="Distribution of Community Sizes";
		Highcharts.chart("communitySizeDistribution", chart);
		
	});
}

function getDegreeDistribution() {
	$.get("DegreeDistribution", function(dataStr) {
		var chart = getHighchartsJSONObject("column");
		var data = JSON.parse(dataStr);
		
		chart.xAxis.categories = data.categories;
		chart.series.push( data.series);
		chart.legend = {}
		chart.legend.enabled = false;
		chart.title.text="Distribution of Degree of vertex";
		Highcharts.chart("degreeDistribution", chart);
	});
}

function getCommunityInfo(d) {
	$.get("communityinfo?id="+d.id, function(dataStr){
		$("#hover-dialog").dialog( "open" );
		data = JSON.parse(dataStr);
		$("#sum-weights").text(data.sumWeights);
		$("#num-nodes").text(data.numNodes);
		var chart = getHighchartsJSONObject("column");
		var data = JSON.parse(dataStr);
		
		chart.xAxis.categories = data.categories;
		chart.series.push( data.series);
		chart.legend = {}
		chart.legend.enabled = false;
		chart.title.text="Distribution of Genres in Community: "+d.id;
		Highcharts.chart("communityGenreInfo", chart);
	});
}


$(function(){
	$("#tabs").tabs();
	$( "#hover-dialog" ).dialog({autoOpen: false, height: 500,
	      width: 800, modal: true});

	$("#hover-dialog").dialog( "close" );
	
	movieGenreDistribution();
	getCommunityDistribution();
	
	//Graph Based terminologies
	getDegreeDistribution();

});


function getHighchartsJSONObject(type) {
	if(type == "bar")
		return getBarChart();
	else if(type == "column")
		return getColumnChart();
}

function getBarChart() {
	return {
	    chart: {
	        type: 'bar'
	    },
	    title: {
	        text: ''
	    },
	    subtitle: {
	        text: ''
	    },
	    xAxis: {
	        categories: [],
	        labels: {
                style: {
                    fontSize:'15px'
                }
            }
	    },
	    yAxis: {
	        title: {
	            text: ''
	        },
	        labels: {
                style: {
                    fontSize:'15px'
                }
            }
	    },
	    series: []
	}
}

function getColumnChart() {
	return {
	    chart: {
	        type: 'column'
	    },
	    title: {
	        text: ''
	    },
	    subtitle: {
	        text: ''
	    },
	    xAxis: {
	        categories: [ ],
	        labels: {
                style: {
                    fontSize:'15px'
                }
            }
	    },
	    yAxis: {
	        title: {
	            text: ''
	        },
	        labels: {
                style: {
                    fontSize:'15px'
                }
            }
	    },
	    series: []
	}
}