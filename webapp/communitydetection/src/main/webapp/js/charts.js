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

$(function(){
	getCommunityDistribution();

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