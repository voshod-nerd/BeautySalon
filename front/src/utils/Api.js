import { API_BASE_URL, ACCESS_TOKEN, API_BASE_IMAGE_URL } from '../constants/index';


const request = (options) => {
    const headers = new Headers({
        'Accept': 'application/json',
        'Content-Type': 'application/json',
    })

    if (localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = { headers: headers };
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
        .then(response =>
            response.json().then(json => {
                if (!response.ok) {
                    return Promise.reject(json);
                }
                return json;
            })
        );
};

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + "/auth/signin",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: API_BASE_URL + "/auth/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}


export function getAllServices() {
    return request({
        url: API_BASE_URL + "/public/service/all",
        method: 'GET'
    });
}

export function getAllMasters() {
    return request({
        url: API_BASE_URL + "/public/master/all",
        method: 'GET'
    });
}

// Booking block 
export function addBooking(value) {
    return request({
        url: API_BASE_URL + "/booking/add",
        method: 'POST',
        body: JSON.stringify(value)
    });
}

export function updateBooking(booking) {
    return request({
        url: API_BASE_URL + "/booking/update",
        method: 'PUT',
        body: JSON.stringify(booking)
    });
}

export function closeBooking(booking) {
    return request({
        url: API_BASE_URL + "/booking/close",
        method: 'PUT',
        body: JSON.stringify(booking)
    });
}

export function getAllBooking() {
    return request({
        url: API_BASE_URL + "/booking/all",
        method: 'GET'
    });
}

export function getBookingByUserAndType(userId, type) {
    return request({
        url: API_BASE_URL + "/booking/byUserId?status=" + type + "&userId=" + userId,
        method: 'GET'
    });
}

export function doBookingListDone(userId) {
    return request({
        url: API_BASE_URL + "/booking/done?userId=" + userId,
        method: 'GET'
    });
}

export function getBookingByDate(date) {
    return request({
        url: API_BASE_URL + "/booking/bydate/" + date,
        method: 'GET'
    });
}

export function getBookingByUser(userid) {
    return request({
        url: API_BASE_URL + "/booking/byuserId/" + userid,
        method: 'GET'
    });
}

// # Раблта с пользователями
export function getAllUsers() {
    return request({
        url: API_BASE_URL + "/users/all",
        method: 'GET'
    });
}
export function updateUser(user) {
    return request({
        url: API_BASE_URL + "/users/update",
        method: 'PUT',
        body: JSON.stringify(user)
    });
}
// #Работа с материалами
export function getAllMaterial() {
    return request({
        url: API_BASE_URL + "/material/all",
        method: 'GET'
    });
}

export function minusMaterial(item) {
    return request({
        url: API_BASE_URL + "/material/minus",
        method: 'POST',
        body: JSON.stringify(item)
    });
}

export function plusMaterial(item) {
    return request({
        url: API_BASE_URL + "/material/plus",
        method: 'POST',
        body: JSON.stringify(item)
    });
}

export function addMaterial(item) {
    return request({
        url: API_BASE_URL + "/material/add",
        method: 'POST',
        body: JSON.stringify(item)
    });
}

// Работа с потребляемыми материалами 
export function getConsumeListByServiceId(value) {
    return request({
        url: API_BASE_URL + "/consume/byServiceId?serviceId=" + value,
        method: 'GET'
    });
}

export function deleteConsumeMaterial(value) {
    return request({
        url: API_BASE_URL + "/consume/delete",
        method: 'DELETE',
        body: JSON.stringify(value)
    });
}

export function addConsume(value) {
    return request({
        url: API_BASE_URL + "/consume/add",
        method: 'POST',
        body: JSON.stringify(value)
    });
}

// ## Работа с мастерами 
export function updateMaster(master) {
    return request({
        url: API_BASE_URL + "/master/update",
        method: 'PUT',
        body: JSON.stringify(master)
    });
}
// image getting 
export function gettingImage(imageName) {
    if ((imageName === undefined) || (imageName === null)) return "";
    return API_BASE_IMAGE_URL + imageName;
}
export function getUploadUrl() {
    return API_BASE_URL + "/public/upload";
}
// Discount API 
export function getAllNonPersonalDiscount() {
    return request({
        url: API_BASE_URL + "/public/discounts/all",
        method: 'GET'
    });
}
export function getAllDiscount() {
    return request({
        url: API_BASE_URL + "/discount/all",
        method: 'GET'
    });
}
export function addDiscount(value) {
    return request({
        url: API_BASE_URL + "/discount/add",
        method: 'POST',
        body: JSON.stringify(value)
    });
}
export function deleteDiscount(value) {
    return request({
        url: API_BASE_URL + "/discount/delete",
        method: 'DELETE',
        body: JSON.stringify(value)
    });
}
// ОПерациии с транзацкциями
export function getAllTransaction() {
    return request({
        url: API_BASE_URL + "/transaction/operation_list",
        method: 'GET'
    });
}

export function getBalance() {
    return request({
        url: API_BASE_URL + "/transaction/balance",
        method: 'GET'
    });
}

export function transactionPerform(value) {
    return request({
        url: API_BASE_URL + "/transaction/perform",
        method: 'POST',
        body: JSON.stringify(value)
    });
}


