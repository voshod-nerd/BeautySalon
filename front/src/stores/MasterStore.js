import { makeAutoObservable } from "mobx";
import { getBookingByDate } from '../utils/Api';

class MasterStore {
    sheduleItems = {
        masters: [],
        items: [],
    }
    booking = [];

    constructor() {
        makeAutoObservable(this);
    }

    getBooking(master) {
        return this.booking.filter(x => x.master.id == master.id);
    }

    loadBookingByMasterAndDate = async (master, date) => {
        var stringDate = (date !== null && date !== undefined) ? date.toISOString().substring(0, 10) : new Date().toISOString().substring(0, 10);
        console.info("Request to server date=" + stringDate + " master =" + JSON.stringify(master));
        this.booking = await getBookingByDate(stringDate);
        console.info(JSON.stringify(this.booking));
        this.sheduleItems.items = [];
        this.sheduleItems.masters = [];
        let value = {
            masters: [],
            items: [],
        }
        value.masters.push({ id: master.id, title: master.name });
        this.booking.filter(x => x.master.id == master.id)
            .forEach(x => value.items.push(
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


}

export default new MasterStore();