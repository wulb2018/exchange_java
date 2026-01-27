

// var chartDom1 = document.getElementById('exchange-chart-new');
// console.log(chartDom1)
// var myChart1 = echarts.init(chartDom1);
// console.log('------------------')   
// var option1;

const upColor = '#00da3c';
const downColor = '#ec0000';
const colorGreen = '#47b262';
const colorRed = '#eb5454';
var orderData = [];
var depthHighData = [];
var depthLowData = [];
var categoryData = [];
var candlestickSplitData = {};
var values = [];
var volumes = [];
let depthCount =0;

const colorGreenOpacity = 'rgba(71, 178, 98, 0.2)';
const colorRedOpacity = 'rgba(235, 84, 84, 0.2)';
function splitData(rawData) {
  for (let i = 0; i < rawData.length; i++) {
    categoryData.push(rawData[i].splice(0, 1)[0]);
    //console.log(rawData[i]);
    values.push(rawData[i]);
    volumes.push([i, rawData[i][4], rawData[i][0] > rawData[i][1] ? 1 : -1]);
  }
  return {
    categoryData: categoryData,
    values: values,
    volumes: volumes
  };
}

function appendSplitData(rawData) {
  for (let i = 0; i < rawData.length; i++) {
    const newBar = rawData[i];
    const lastIndex = categoryData.length - 1;
    const lastTime = categoryData[lastIndex];

    if (lastTime === newBar[0]) {
        values[values.length - 1] = [
            newBar[1],
            newBar[2],
            newBar[3],
            newBar[4],
        ];
        volumes[volumes.length - 1] = [
            volumes.length - 1,
            newBar[5],
            volumes[volumes.length - 1][2]
        ];
    } else {
        categoryData.push(newBar.splice(0, 1)[0]);
        //console.log(newBar);
        values.push(newBar);
        volumes.push([volumes.length + 1, newBar[4], newBar[0] > newBar[1] ? 1 : -1]);
    }
    return {
        categoryData: categoryData,
        values: values,
        volumes: volumes
    };
  }
  return {
    categoryData: categoryData,
    values: values,
    volumes: volumes
  };
}


function candlestickDataListToArray(candlestickList) {
    const arr = [];
    for(let i = 0; i < candlestickList.length; i++) {
        const candlestick = candlestickList[i];
        arr.push([
            candlestick.datetimeCategory,
            candlestick.openPrice,
            candlestick.closePrice,
            candlestick.lowestPrice,
            candlestick.highestPrice,
            candlestick.volume
        ]);
        
    }
    //console.log(arr);
    return arr;
}

function calculateMA(dayCount, data) {
  var result = [];
  for (var i = 0, len = data.values.length; i < len; i++) {
    if (i < dayCount) {
      result.push('-');
      continue;
    }
    var sum = 0;
    for (var j = 0; j < dayCount; j++) {
      sum += data.values[i - j][1];
    }
    result.push(+(sum / dayCount).toFixed(3));
  }
  return result;
}
const priceFormatter = (value) => {
    const result = Math.round(value * 100) / 100 + '';
    // Adding padding 0 if needed
    let dotIndex = result.indexOf('.');
    if (dotIndex < 0) {
        return result + '.00';
    } else if (dotIndex === result.length - 2) {
        return result + '0';
    }
    return result;
};

function pushOrderData(buyOrderBook, isLower) {
    if (buyOrderBook == undefined) {
        return;
    }
    const orderPrice = buyOrderBook['orderPrice'];

    const amount = buyOrderBook.amount;
    orderData.push({
        value: amount,
        itemStyle: {
            color: isLower ? colorGreenOpacity : colorRedOpacity
        },
        label: {
            formatter:
                `{name|${isLower ? 'Bid' : 'Ask'}} ` +
                `{${isLower ? 'green' : 'red'}|${priceFormatter(orderPrice)}} ` +
                `{amount|(${amount})}`,
            rich: {
                red: {
                    color: colorRed
                },
                green: {
                    color: colorGreen
                },
                amount: {
                    color: '#666'
                },
                name: {
                    fontWeight: 'bold',
                    color: '#444'
                }
            }
        }
    });
}

function buildOrderBookAndDepthData(buyOrderBookList, sellOrderBookList) {
    const depthCount = Math.max(buyOrderBookList.length, sellOrderBookList.length);
    depthHighData = [];
    depthLowData = [];
    orderData = [];
    for(let i = 0; i < buyOrderBookList.length; i++) {
        const isLower = true;
        const buyOrderBook = buyOrderBookList[i];
        if (buyOrderBook == undefined) {
            continue;
        }
        pushOrderData(buyOrderBook, isLower);
        //买方是depthHighData
        depthHighData[depthCount + i] = buyOrderBook.cumulativeQuantity;
    }
    for(let i = 0; i < sellOrderBookList.length; i++) {
        const isLower = false;
        const sellOrderBook = sellOrderBookList[i];
        if (sellOrderBook == undefined) {
            continue;
        }
        pushOrderData(sellOrderBook, isLower);

        //卖方是depthLowData
        depthLowData[depthCount - i - 1] = sellOrderBook.cumulativeQuantity;
    }
}

var getOption = function(myChart,dataMap) {
    let option;
    

    buildOrderBookAndDepthData(dataMap.buyOrderBookList, dataMap.sellOrderBookList);
    
    // for(let i = 0; i < dataMap.buyOrderBookList.length; i++) {
    //     const isLower = true;
    //     const buyOrderBook = dataMap.buyOrderBookList[i];
    //     if (buyOrderBook == undefined) {
    //         continue;
    //     }
    //     pushOrderData(buyOrderBook, isLower);
    //     //买方是depthHighData
    //     depthHighData[depthCount + i] = buyOrderBook.cumulativeQuantity;
    // }
    // for(let i = 0; i < dataMap.sellOrderBookList.length; i++) {
    //     const isLower = false;
    //     const sellOrderBook = dataMap.sellOrderBookList[i];
    //     if (sellOrderBook == undefined) {
    //         continue;
    //     }
    //     pushOrderData(sellOrderBook, isLower);

    //     //卖方是depthLowData
    //     depthLowData[depthCount - i - 1] = sellOrderBook.cumulativeQuantity;
    // }

    let candlestickRawData = candlestickDataListToArray(dataMap.candlestickList);
    candlestickSplitData = splitData(candlestickRawData);
    myChart.setOption(
        (option = {
        animation: false,
        legend: {
            bottom: 10,
            left: 'center',
            data: ['Dow-Jones index', 'MA5', 'MA10', 'MA20', 'MA30']
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
            type: 'cross'
            },
            borderWidth: 1,
            borderColor: '#ccc',
            padding: 10,
            textStyle: {
            color: '#000'
            },
            position: function (pos, params, el, elRect, size) {
            const obj = {
                top: 10
            };
            obj[['left', 'right'][+(pos[0] < size.viewSize[0] / 2)]] = 30;
            return obj;
            }
            // extraCssText: 'width: 170px'
        },
        axisPointer: {
            link: [
            {
                xAxisIndex: 'all'
            }
            ],
            label: {
            backgroundColor: '#777'
            }
        },
        toolbox: {
            feature: {
            dataZoom: {
                yAxisIndex: false
            },
            brush: {  
                type: ['lineX', 'clear']
            }
            }
        },
        brush: {
            xAxisIndex: 'all',
            brushLink: 'all',
            outOfBrush: {
            colorAlpha: 0.1
            }
        },
        visualMap: {
            show: false,
            seriesIndex: 5,
            dimension: 2,
            pieces: [
            {
                value: 1,
                color: downColor
            },
            {
                value: -1,
                color: upColor
            }
            ]
        },
        grid: [
            {
            left: '10%',
            right: '20%',
            height: '50%'
            },
            {
            left: '10%',
            right: '20%',
            top: '63%',
            height: '16%'
            },
            {
            left: '83%',
            right: '0%',
            height: '50%'
            },
            {
            left: '83%',
            right: '0%',
            top: '63%',
            height: '16%'
            },
        ],
        xAxis: [
            {
                id:0,
                type: 'category',
                data: candlestickSplitData.categoryData,
                boundaryGap: false,
                axisLine: { onZero: false },
                splitLine: { show: false },
                min: 'dataMin',
                max: 'dataMax',
                axisPointer: {
                    z: 100
                }
            },
            {
                id:1,
                type: 'category',
                gridIndex: 1,
                data: candlestickSplitData.categoryData,
                boundaryGap: false,
                axisLine: { onZero: false },
                axisTick: { show: false },
                splitLine: { show: false },
                axisLabel: { show: false },
                min: 'dataMin',
                max: 'dataMax'
            },
            {
                id:2,
                type: 'value',
                gridIndex: 2,
                show: false,
                max: 'dataMax'
            },
            {
                id:3,
                type: 'category',
                gridIndex: 3,
                show: false,
                boundaryGap: false,
                data: Array.from({ length: depthCount * 2 }, (_, i) => i + '')
            }
        ],
        yAxis: [
            {
                scale: true,
                splitArea: {
                    show: true
                }
            },
            {
                scale: true,
                gridIndex: 1,
                splitNumber: 2,
                axisLabel: { show: false },
                axisLine: { show: false },
                axisTick: { show: false },
                splitLine: { show: false }
            },
            {
                type: 'category',
                gridIndex: 2,
                show: false
            },
            {
                type: 'value',
                gridIndex: 3,
                show: false,
                max: 'dataMax',
                min: 'dataMin'
            }
        ],
        dataZoom: [
            {
            type: 'inside',
            xAxisIndex: [0, 1],
            start: 98,
            end: 100
            },
            {
            show: true,
            xAxisIndex: [0, 1],
            type: 'slider',
            top: '85%',
            start: 98,
            end: 100
            }
        ],
        series: [
            {
                id:0,
                name: 'Dow-Jones index',
                type: 'candlestick',
                data: candlestickSplitData.values,
                itemStyle: {
                    color: upColor,
                    color0: downColor,
                    borderColor: undefined,
                    borderColor0: undefined
                }
            },
            {
                id:1,
                name: 'MA5',
                type: 'line',
                data: calculateMA(5, candlestickSplitData),
                smooth: true,
                lineStyle: {
                    opacity: 0.5
                }
            },
            {
                id:2,
                name: 'MA10',
                type: 'line',
                data: calculateMA(10, candlestickSplitData),
                smooth: true,
                lineStyle: {
                    opacity: 0.5
                }
            },
            {
                id:3,
                name: 'MA20',
                type: 'line',
                data: calculateMA(20, candlestickSplitData),
                smooth: true,
                lineStyle: {
                    opacity: 0.5
                }
            },
            {
                id:4,
                name: 'MA30',
                type: 'line',
                data: calculateMA(30, candlestickSplitData),
                smooth: true,
                lineStyle: {
                    opacity: 0.5
                }
            },
            {
                id:5,
                name: 'Volume',
                type: 'bar',
                xAxisIndex: 1,
                yAxisIndex: 1,
                data: candlestickSplitData.volumes
            },
            {
                id:6,
                name: 'Order Book',
                type: 'bar',
                xAxisIndex: 2,
                yAxisIndex: 2,
                data: orderData,
                barWidth: '90%',
                label: {
                    show: true,
                    position: 'insideLeft'
                }
            },
            {
                id:7,
                name: 'Depth High',
                type: 'line',
                xAxisIndex: 3,
                yAxisIndex: 3,
                data: depthHighData,
                step: 'end',
                lineStyle: {
                    color: colorRed,
                    width: 2
                },
                areaStyle: {
                    color: colorRedOpacity,
                    opacity: 1
                },
                symbol: 'none'
            },
            {
                id:8,
                name: 'Depth Low',
                type: 'line',
                xAxisIndex: 3,
                yAxisIndex: 3,
                data: depthLowData,
                step: 'end',
                lineStyle: {
                    color: colorGreen,
                    width: 2
                },
                areaStyle: {
                    color: colorGreenOpacity,
                    opacity: 1
                },
                symbol: 'none'
            }
        ]
        }),
        true
    );
    myChart.dispatchAction({
        type: 'brush',
        areas: [
        {
            brushType: 'lineX',
            coordRange: ['2016-06-02', '2016-06-20'],
            xAxisIndex: 0
        }
        ]
    });

}


//option1 && myChart1.setOption(option1);
