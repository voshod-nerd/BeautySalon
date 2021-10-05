import { makeAutoObservable } from "mobx";
import { getAllServices, getAllUsers, getAllMaterial, closeBooking, updateMaster, updateBooking, getAllDiscount, addDiscount, getBookingByDate, getAllBooking, deleteDiscount } from '../utils/Api';
import { transactionPerform, getBalance, updateUser, getAllTransaction, deleteConsumeMaterial, doBookingListDone, addConsume, getConsumeListByServiceId } from '../utils/Api';
import { minusMaterial, plusMaterial, addMaterial } from '../utils/Api';

class AdminStore {
    services = [];
    users = [];
    materials = [];
    discount = [];
    booking = [];
    balance = [];
    transaction = [];
    bookingRequest = [];
    consumeList = [];

    constructor() {
        makeAutoObservable(this);
    }

    get getUsers() {
        return this.users.filter(x => x.role == "ROLE_USER");
    }

    get getAllUsers() {
        return this.users;
    }

    get getMasters() {
        return this.users.filter(x => x.role == "ROLE_MASTER");
    }

    get getServices() {
        return this.services;
    }

    get getMaterials() {
        return this.materials;
    }

    get getDiscount() {
        return this.discount;
    }

    get getBooking() {
        return this.booking;
    }

    get getTransaction() {
        return this.transaction;
    }

    get getCashier() {
        return this.balance;
    }

    get getConsumeList() {
        return this.consumeList;
    }

    reload = async () => {
        console.log("Reload begin");
        await this.loadServices();
        await this.loadDiscount();
        await this.loadMaterial();
        await this.loadBooking();
        await this.loadTransaction();
        await this.loadUsers();
        await this.loadBalance();
        console.log("Reload end");
    }

    // Begin load section
    loadServices = async () => {
        this.services = await getAllServices();
    }

    loadDiscount = async () => {
        this.discount = await getAllDiscount();
    }

    loadMaterial = async () => {
        this.materials = await getAllMaterial();
    }

    loadUsers = async () => {
        this.users = await getAllUsers();

    }

    loadBooking = async () => {
        this.booking = await getAllBooking();

    }

    loadBookingByDate = async (date) => {
        var string = (date !== null && date !== undefined) ? date.toISOString().substring(0, 10) : new Date().toISOString().substring(0, 10);
        this.booking = await getBookingByDate(string);
    }

    loadBalance = async () => {
        let response = await getBalance();
        this.balance = response.body;
        return this.balance;
    }

    loadTransaction = async () => {
        this.transaction = await getAllTransaction();

    }

    // ## End load section
    getBookingByUserIdAndType = (userId, type) => {
        return this.booking.filter(x => x.master.id == userId)
            .filter(x => x.statusBooking == type);
    }

    getMaterialByServiceId = async (serviceId) => {
        this.consumeList = await getConsumeListByServiceId(serviceId);
    }

    addConsumeMaterial = async (value) => {
        await addConsume(value)
            .then(response => { this.reload(); this.getMaterialByServiceId(value.serviceId); })
            .catch(error => console.log(error));
    }


    getPromokodByName = (value) => {

        let discountList = this.discount
            .filter(x => x.type == "PROMOCOD")
            .filter(x => x.name == value);
        if (discountList.length >= 1) return discountList[0].value;
        else return 0;
    }

    editMaster = (master) => {
        updateMaster(master).then(response => this.loadUsers()).catch(err => console.log(err));
    }

    editUser = (value) => {
        updateUser(value)
            .then(response => this.reload())
            .catch(error => console.log(error));
    }

    addDiscount = async (value) => {
        await addDiscount(value)
            .then(response => this.reload())
            .catch(error => console.log(error));
    }

    minusMaterialCount = async (value) => {
        await minusMaterial(value)
            .then(response => { this.reload(); })
            .catch(error => console.log(error));
    }

    plusMaterialCount = async (value) => {
        await plusMaterial(value)
            .then(response => { this.reload(); })
            .catch(error => console.log(error));
    }
    addNewMaterial = async (value) => {
        await addMaterial(value)
            .then(response => { this.reload(); })
            .catch(error => console.log(error));
    }

    deleteConsume = async (value) => {

        await deleteConsumeMaterial(value)
            .then(response => { this.reload(); this.getMaterialByServiceId(value.serviceId); })
            .catch(error => console.log(error));
    }

    deleteDiscount = async (value) => {

        await deleteDiscount(value)
            .then(response => this.reload())
            .catch(error => console.log(error));
    }

    cancelBooking = (value) => {
        value.statusBooking = "CANCELED";
        updateBooking(value)
            .then(response => this.reload())
            .catch(error => console.log(error));
    }

    apporovedBooking = (value) => {
        value.statusBooking = "APPROVED";
        updateBooking(value)
            .then(response => this.reload())
            .catch(error => console.log(error));
    }

    doneBooking = (userId) => {
        doBookingListDone(userId)
            .then(response => this.reload())
            .catch(error => console.log(error));
    }

    closeBooking = (value) => {
        value.statusBooking = "PAYED";
        closeBooking(value).then(response => this.reload())
            .catch(error => console.log(error));
    }

    makeTransaction = async (value) => {
        await transactionPerform(value)
            .then(response => this.reload())
            .catch(error => console.log(error));
        return true;
    }


}

export default new AdminStore();