import React, { useEffect, useContext, useState } from 'react';
import { Statistic, Button, Typography, Divider, Collapse, Form, Col, Row, DatePicker } from 'antd';
import AdminContext from '../../contexts/AdminContext';
import moment from 'moment';
import Pdf from "react-to-pdf";
import { observer } from "mobx-react";
import uuid from 'react-uuid';
import { Column, Pie } from '@ant-design/charts';
const { Panel } = Collapse;
const { Title } = Typography;
const ref = React.createRef();

const config = {
    data: [],
    width: 400,
    height: 200,
    autoFit: false,
    xField: 'name',
    yField: 'value',
    label: {
        style: {
            fill: '#aaa',
        },
    },
};

const configMoney = {
    data: [],
    width: 400,
    height: 200,
    autoFit: false,
    xField: 'name',
    yField: 'value',
    label: {
        style: {
            fill: '#aaa',
        },
    },
};

const configMasterBar = {
    data: [],
    width: 400,
    height: 200,
    autoFit: false,
    xField: 'name',
    yField: 'value',
    label: {
        style: {
            fill: '#aaa',
        },
    },
};

const configMasterMoneyPie = {
    data: [],
    width: 400,
    height: 200,
    autoFit: false,
    angleField: 'value',
    colorField: 'name',
};

const configMoneyBarMonth = {
    data: [],
    width: 400,
    height: 200,
    autoFit: false,
    xField: 'name',
    yField: 'value',
    label: {
        style: {
            fill: '#aaa',
        },
    },
};
const configCountBarMonth = {
    data: [],
    width: 400,
    height: 200,
    autoFit: false,
    xField: 'name',
    yField: 'value',
    label: {
        style: {
            fill: '#aaa',
        },
    },
};


let chart;
let chartMoney;
let masterColumn;
let pie;
let barMonth;
let barCountMonth;

const Stats = observer(() => {
    const [sum, setSum] = useState(0);
    const [begin, setBegin] = useState(new Date());
    const [end, setEnd] = useState(new Date());
    const adminStore = useContext(AdminContext);
    useEffect(() => reload());
    const reload = () => { adminStore.loadBooking(); }

    const onChangeValue = (type, date) => {
        if (type === "END") setEnd(date);
        if (type === "BEGIN") setBegin(date);

        masterStatistic();
        mostPopularService();
        masterStatisticMoney();
        statisticMoneyMonth();
        statisticCountMonth();

    }

    const filterDate = (value, begin, end) => {
        if (value === undefined || value === null) return false;
        let date = new Date(value.dateB);
        if (date.valueOf() > begin.valueOf() && date.valueOf() < end.valueOf()) return true;
        else return false;
    }


    const mostPopularService = () => {
        let filterArr = adminStore.getBooking.filter(x => filterDate(x, begin, end));
        if (filterArr.length === 0) return "";
        let listAllService = [];
        for (let i = 0; i < filterArr.length; i++) filterArr[i].serviceList.forEach(element => {
            listAllService.push(element);
        });
        var result = listAllService.reduce(function (acc, el) {
            acc[el.name] = (acc[el.name] || 0) + 1;
            return acc;
        }, {});

        var resultMoney = listAllService.reduce(function (acc, el) {
            acc[el.name] = (acc[el.name] || 0) + el.price;
            return acc;
        }, {});

        let res = [];
        for (let key in result) {
            res.push({
                name: key,
                value: result[key]
            })
        }
        let resMoney = [];
        for (let key in resultMoney) {
            resMoney.push({
                name: key,
                value: resultMoney[key]
            })
        }
        configMoney.data = resMoney;
        config.data = res;
    }

    const masterStatistic = () => {
        let filterArr = adminStore.getBooking.filter(x => filterDate(x, begin, end));
        if (filterArr.length === 0) return "";
        var result = filterArr.reduce(function (acc, el) {
            acc[el.master.name] = (acc[el.master.name] || 0) + el.serviceList.length;
            return acc;
        }, {});
        let res = [];
        for (let key in result) {
            res.push({
                name: key,
                value: result[key]
            })
        }
        configMasterBar.data = res;
    }


    const masterStatisticMoney = () => {
        let filterArr = adminStore.getBooking.filter(x => filterDate(x, begin, end));
        if (filterArr.length === 0) return "";
        var result = filterArr.reduce(function (acc, el) {
            acc[el.master.name] = (acc[el.master.name] || 0) + el.totalSum;
            return acc;
        }, {});
        let res = [];
        for (let key in result) {
            res.push({
                name: key,
                value: result[key]
            })
        }
        console.log(res);
        configMasterMoneyPie.data = res;
        setSum(res.reduce(function (a, b) {
            return a.value + b.value;
        }));
    }


    const statisticMoneyMonth = () => {
        let filterArr = adminStore.getBooking.filter(x => filterDate(x, begin, end));
        if (filterArr.length == 0) return;
        var result = filterArr.reduce(function (acc, el) {
            let monthAndYear = moment(el.dateB).format('MMMM') + '-' + moment(el.dateB).year();
            acc[monthAndYear] = (acc[monthAndYear] || 0) + el.totalSum;
            return acc;
        }, {});
        console.log(result);

        let res = [];
        for (let key in result) {
            res.push({
                name: key,
                value: result[key]
            })
        }
        configMoneyBarMonth.data = res;
    }

    const statisticCountMonth = () => {
        let filterArr = adminStore.getBooking.filter(x => filterDate(x, begin, end));
        if (filterArr.length == 0) return;
        var result = filterArr.reduce(function (acc, el) {
            let monthAndYear = moment(el.dateB).format('MMMM') + '-' + moment(el.dateB).year();
            acc[monthAndYear] = (acc[monthAndYear] || 0) + el.serviceList.length;
            return acc;
        }, {});
        let res = [];
        for (let key in result) {
            res.push({
                name: key,
                value: result[key]
            })
        }
        configCountBarMonth.data = res;
    }

    return (
        <div >
            <Form layout="vertical">
                <Form.Item label="Начала периода формирования статистики"> <DatePicker onChange={(value) => onChangeValue('BEGIN', value)}></DatePicker></Form.Item>
                <Form.Item label="Конец период формирования статистики"> <DatePicker onChange={(value) => onChangeValue('END', value)}></DatePicker></Form.Item>
            </Form>
            <Pdf targetRef={ref} filename="div-blue.pdf" x={.6} y={.5} scale={0.9}>
                {({ toPdf }) => (
                    <Button type="primary" onClick={toPdf}>Cформировать PDF файл</Button>
                )}
            </Pdf>

            <div ref={ref}>
                <Row gutter={24}>
                    <Col span={8}>
                        <Statistic title="Всего за период заработано" value={sum} />
                    </Col>
                    <Col span={8}>
                        <Statistic title="Начала периода" value={moment(begin).format('l')} />
                    </Col>
                    <Col span={8}>
                        <Statistic title="Конец периода" value={moment(end).format('l')} />
                    </Col>
                </Row>

                <Collapse >
                    <Panel header="Услуги по частоте" key="1">
                        <Column {...config} onReady={(chartInstance) => (chart = chartInstance)} />
                    </Panel>
                    <Panel header="Услуги по суммам" key="2">
                        <Column {...configMoney} onReady={(chartInstance) => (chartMoney = chartInstance)} />
                    </Panel>
                    <Panel header="Количество услуг оказанных мастерами" key="3">
                        <Column {...configMasterBar} onReady={(chartInstance) => (masterColumn = chartInstance)} />
                    </Panel>
                    <Panel header="Количество денег заработанных мастерами" key="4">
                        <Pie {...configMasterMoneyPie} onReady={(chartInstance) => (pie = chartInstance)} />
                    </Panel>
                    <Panel header="Помесячный график прибыли и количества услуг" key="5">
                        <Row>
                            <Col span={7}>
                                <Title level={5}>График прибыли</Title>
                                <Column {...configMoneyBarMonth} onReady={(chartInstance) => (barMonth = chartInstance)} />
                            </Col>
                            <Col span={7}>
                                <Title level={5}>График количества оказанных услуг</Title>
                                <Column {...configCountBarMonth} onReady={(chartInstance) => (barCountMonth = chartInstance)} />
                            </Col>
                        </Row>
                    </Panel>
                </Collapse>

            </div>
            <Divider></Divider>
        </div>);
});

export default Stats;