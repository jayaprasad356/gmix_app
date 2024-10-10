package com.g_mix.gmw_app.helper

object Constant {
 const val AppPlayStoreUrl: String = ""
//   const val MainBaseUrl: String = "https://gmix.graymatterworks.com/"
    const val MainBaseUrl: String = "https://testgmix.graymatterworks.com/"
 const val PAYMENT_LINK: String = "https://gateway.graymatterworks.com/api/create_payment_request.php"


 const val BaseUrl: String = MainBaseUrl + "api/"


    const val LOGIN: String = BaseUrl + "login"
    const val OTP: String = BaseUrl + "otp"
    const val USERDETAILS: String = BaseUrl + "userdetails"
    const val UPDATE_RESELLS: String = BaseUrl + "update_resells"
    const val ADD_ADDRESS: String = BaseUrl + "add_address"
    const val PRODUCT_LIST: String = BaseUrl + "product_list"
    const val ORDERS_LIST: String = BaseUrl + "orders_list"
    const val UPDATE_REVIEWS: String = BaseUrl + "update_reviews"
    const val UPDATE_RATINGS: String = BaseUrl + "update_ratings"
    const val REWARD_LIST: String = BaseUrl + "reward_product_list"
    const val PLACE_ORDER: String = BaseUrl + "place_order"
    const val MY_ADDRESS_LIST: String = BaseUrl + "my_address_list"
    const val ADDRESS_DETAILS: String = BaseUrl + "address_details"
    const val REVIEWS_LIST: String = BaseUrl + "reviews_list"
    const val DELETE_ADDRESS: String = BaseUrl + "delete_address"
    const val UPDATE_ADDRESS: String = BaseUrl + "update_address"
    const val PINCODE_URL: String = BaseUrl + "pincode"
    const val SETTING: String = BaseUrl + "settings_list"
    const val PRIVACY_POLICY: String = BaseUrl + "privacy_policy"
    const val REFUND_POLICY: String = BaseUrl + "refund_policy"
    const val TERMS_CONDITION: String = BaseUrl + "terms_conditions"
    const val IMAGE_SLIDERS: String = BaseUrl + "image_sliders"
    const val CATEGORY_PRODUCT_LIST: String = BaseUrl + "category_product_list"
    val APPUPDATE: String = BaseUrl + "appsettings_list"


    fun getOTPUrl(key: String, mobile: String, otp: String): String {
        return "https://api.authkey.io/request?authkey=$key&mobile=$mobile&country_code=91&sid=14324&otp=$otp"
    }

//    https://api.authkey.io/request?authkey=64045a300411033f&mobile=8219998573&country_code=91&sid=14324&otp=123456

    const val AUTHORIZATION: String = "Authorization"
    const val TOKEN: String = "token"
    const val USER_ID: String = "user_id"
    const val CATEGORY_ID: String = "category_id"
    const val PRODUCT_ID: String = "product_id"
    const val ADDRESS_ID: String = "address_id"
    const val ID: String = "id"
    const val LIMIT: String = "limit"
    const val POINTS: String = "points"
    const val TOTAL: String = "total"
    const val OFFSET: String = "offset"
    const val MOBILE: String = "mobile"
    const val PLACE: String = "place"
    const val QUALIFICATION: String = "qualification"
    const val EXPERIENCE: String = "experience"
    const val AGE: String = "age"
    const val GENDER: String = "gender"
    const val REVIEWS: String = "reviews"
    const val RATINGS: String = "ratings"
    const val ORDER_ID: String = "order_id"


    const val IS_LOGIN: String = "is_login"
    const val PRICE: String = "price"
    const val PAYMENT_MODE: String = "payment_mode"
    const val QUANTITY: String = "quantity"
    const val DELIVERY_CHARGE: String = "delivery_charges"
    const val CUSTOMER_SUPPORT_NUMBER: String = "customer_support_number"
    const val UPI_ID: String = "upi_id"
    const val PAYMENT_IMAGE: String = "payment_image"
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