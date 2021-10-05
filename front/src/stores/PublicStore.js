import { makeAutoObservable, runInAction } from "mobx";
import { getAllServices, getAllMasters, addBooking, updateBooking, getAllBooking, getBookingByDate, getBookingByUser, getAllNonPersonalDiscount } from '../utils/Api';
import { BOOKING_ERROR } from '../constants/index';
import { configure } from "mobx"
import moment from 'moment';

configure({
    enforceActions: "never",
})

class PublicStore {
    masters = [];
    services = [];
    booking = [];
    сurBooking = [];
    discount = [];
    userBooking = []
    sheduleItems = {
        masters: [],
        items: [],
    }

    constructor() {
        makeAutoObservable(this);
    }

    get masterList() {
        return this.masters;
    }
    get serviceList() {
        return this.services;
    }
    get sheduleItemsList() {
        return this.sheduleItems;
    }

    get getCurrentBooking() {
        return this.сurBooking;
    }

    get getUserBooking() {
        return this.userBooking;
    }

    get getDiscount() {
        return this.discount;
    }


    reloadData() {
        this.loadMasters();
        this.loadServices();
        this.loadBooking();
        this.loadDiscounts();
        this.loadMasters();
    }

    loadBookingByUserId = async (userId) => {
        this.userBooking = await getBookingByUser(userId);
        console.log(this.userBooking);
    }

    loadBookingByDate = async (date) => {
        let string= moment(date).format('YYYY-MM-DD');
        console.log(string);
        this.сurBooking = await getBookingByDate(string);
        this.masters = await getAllMasters();
        let value = {
            masters: [],
            items: [],
        }
        this.masters.forEach(x => value.masters.push({ id: x.id, title: x.name }));
        this.сurBooking.forEach(x => value.items.push(
            {
                id: x.id,
                group: x.master.id,
                title: 'Бронирование № ' + x.id,
                start_time: new Date(x.dateB).getTime(),
                end_time: new Date(x.dateE).getTime(),
            }
        ));
        this.sheduleItems = value;
        return this.sheduleItems;
    }

    loadBooking = async () => {
        let response = await getAllBooking();
        console.log(JSON.stringify(response));
        runInAction(() => {
            this.booking = response
        })
        this.sheduleItems.items = [];
        let value = {
            masters: this.sheduleItems.masters,
            items: [],
        }
        this.booking.forEach(x => value.items.push(
            {
                id: x.id,
                group: x.master.id,
                title: 'Бронирование № ' + x.id,
                start_time: new Date(x.dateB).getTime(),
                end_time: new Date(x.dateE).getTime(),
            }
        ));
        this.sheduleItems = value;
    }

    loadServices = async () => {
        let response = await getAllServices();
        runInAction(() => {
            this.services = response
        })
        console.log(this.services);
    }

    loadMasters = async () => {
        this.masters = await getAllMasters();
        console.log("Clear masters of sheduleItems");
        this.sheduleItems.masters = [];
        this.masters.forEach(x => this.sheduleItems.masters.push({ id: x.id, title: x.name }));
    }

    loadDiscounts = async () => {
        this.discount = await getAllNonPersonalDiscount();
    }

    rateBooking(value, rate) {
        value.rate = rate;
        updateBooking(value)
            .then(response => { this.reloadData(); this.loadBookingByUserId(this.user.id); })
            .catch(error => console.log(error));
    }

    cancelBooking(value) {
        value.statusBooking = "CANCELED";
        updateBooking(value)
            .then(response => console.log(response))
            .catch(error => console.log(error));
    }



    createBooking(serviceItems, masterItem, userItem, dateBooking) {
        let totalsum = serviceItems.reduce((a, b) => a.price + b.price, 0);
        const masterInfo = {
            id: masterItem.id,
            name: masterItem.name
        }
        const userInfo = {
            id: userItem.id,
            name: userItem.username,
        }
        const request = {
            master: masterInfo,
            user: userInfo,
            serviceList: serviceItems,
            dateB: dateBooking,
            sum: totalsum
        }
        console.log("Request = ", request);
        return addBooking(request)
            .then(response => {
                console.log(response);
                return Promise.resolve(response);
            }).catch(error => {
                return Promise.reject(BOOKING_ERROR);
            });

    }
}

export default new PublicStore();
