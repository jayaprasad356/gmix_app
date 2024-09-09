package com.g_mix.gmw_app.helper

object Constant {
    const val AppPlayStoreUrl: String = ""
   // const val MainBaseUrl: String = "https://gmix.graymatterworks.com/"
    const val MainBaseUrl: String = "https://testgmix.graymatterworks.com/"

    const val BaseUrl: String = MainBaseUrl + "api/"


    const val LOGIN: String = BaseUrl + "login"
    const val OTP: String = BaseUrl + "otp"
    const val USERDETAILS: String = BaseUrl + "userdetails"
    const val ADD_ADDRESS: String = BaseUrl + "add_address"
    const val PRODUCT_LIST: String = BaseUrl + "product_list"
    const val ORDERS_LIST: String = BaseUrl + "orders_list"
    const val REWARD_LIST: String = BaseUrl + "reward_product_list"
    const val PLACE_ORDER: String = BaseUrl + "place_order"
    const val MY_ADDRESS_LIST: String = BaseUrl + "my_address_list"
    const val REVIEWS_LIST: String = BaseUrl + "reviews_list"
    const val PINCODE_URL: String = BaseUrl + "pincode"
    const val SETTING: String = BaseUrl + "settings_list"
    val APPUPDATE: String = BaseUrl + "appsettings_list"


    fun getOTPUrl(key: String, mobile: String, otp: String): String {
        return "https://api.authkey.io/request?authkey=$key&mobile=$mobile&country_code=91&sid=14324&otp=$otp"
    }

//    https://api.authkey.io/request?authkey=64045a300411033f&mobile=8219998573&country_code=91&sid=14324&otp=123456

    const val AUTHORIZATION: String = "Authorization"
    const val TOKEN: String = "token"
    const val USER_ID: String = "user_id"
    const val PRODUCT_ID: String = "product_id"
    const val ADDRESS_ID: String = "address_id"
    const val ID: String = "id"
    const val LIMIT: String = "limit"
    const val POINTS: String = "points"
    const val TOTAL: String = "total"
    const val OFFSET: String = "offset"
    const val MOBILE: String = "mobile"
    const val IS_LOGIN: String = "is_login"
    const val PRICE: String = "price"
    const val PAYMENT_MODE: String = "payment_mode"
    const val QUANTITY: String = "quantity"
    const val DELIVERY_CHARGE: String = "delivery_charges"
    const val CUSTOMER_SUPPORT_NUMBER: String = "customer_support_number"
    const val FROM: String = "from"
    const val SUCCESS: String = "success"
    const val MESSAGE: String = "message"
    const val DATA: String = "data"
    const val NAME: String = "name"
    const val VERSION: String = "app_version"
    const val LINK: String = "link"
    const val LAST_NAME: String = "last_name"
    const val FIRST_NAME: String = "first_name"

    const val ALTERNATE_MOBILE: String = "alternate_mobile"
    const val DOOR_NUMBER: String = "door_no"
    const val STREET_NAME: String = "street_name"
    const val LANDMARK: String = "landmark"
    const val PINCODE: String = "pincode"
    const val CITY: String = "city"
    const val STATE: String = "state"

    const val LANGUAGE_CODE: String = "en"
}