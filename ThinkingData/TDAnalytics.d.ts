export default TDAnalytics;
export namespace TDAutoTrackEventType {
    let APP_START: number;
    let APP_END: number;
    let APP_CRASH: number;
    let APP_INSTALL: number;
}
export namespace TDTrackStatus {
    let PAUSE: string;
    let STOP: string;
    let SAVE_ONLY: string;
    let NORMAL: string;
}
export namespace TDThirdPartyType {
    let APPS_FLYER: number;
    let IRON_SOURCE: number;
    let ADJUST: number;
    let BRANCH: number;
    let TOP_ON: number;
    let TRACKING: number;
    let TRAD_PLUS: number;
}
/**
 * @class
 */
declare class TDAnalytics {
    static instances: {};
    /**
     * time calibration with timestamp
     * @param {long} time timestamp
     */
    static calibrateTime(time: long): void;
    /**
     * time calibration with ntp
     * @param {String} ntp_server  ntp server url
     */
    static calibrateTimeWithNtp(ntp_server: string): void;
    /**
     * Initialize the SDK. The track function is not available until this interface is invoked.
     * @param {String} appId app id,required
     * @param {String} serverUrl server url,required
     */
    static init(appId: string, serverUrl: string): void;
    /**
     * Initialize the SDK with config. The track function is not available until this interface is invoked.
     * @param {Object} config init config
     * 
     * @property {String} appId config.appid,required
     * @property {String} serverUrl config.serverUrl,required
     * @property {boolean} enableEncrypt config.enableEncrypt whether to enable encryption,optional
     * @property {Object} secretKey config.secretKey encrypt configuration information,optional
     * @property {boolean} enableLog config.enableLog whether to enable local logging,optional
     * @property {String} timeZone config.timeZone default time zone,optional
     */
    static init(appId: object): void;
    /**
     * Upload a single event, containing only preset properties and set public properties.
     * @param {Object} options event infomations
     *
     * @property {String} eventName options.eventName event name, required
     * @property {Object} properties  options.properties event properties, optional
     * @property {long} time  options.time event timeStamp, optional
     * @property {String} timeZone  options.timeZone event time zone, optional
     * @property {String} appId app id,optional
     */
    static track(options?: any): void;
    /**
     * Sending First Event
     * @param {Object} options event infomations
     *
     * @property {String} eventName options.eventName event name, required
     * @property {Object} properties  options.properties event properties, optional
     * @property {String} eventId  options.eventId event ID, to mark the event, optional
     * @property {long} time  options.time event timeStamp, optional
     * @property {String} timeZone  options.timeZone event time zone, optional
     * @property {String} appId app id,optional
     */
    static trackFirst(options?: any): void;
    /**
     * Sending Updatable Event
     * @param {Object} options event infomations
     *
     * @property {String} eventName options.eventName event name, required
     * @property {Object} properties  options.properties event properties, optional
     * @property {String} eventId  options.eventId event ID, to mark the event, optional
     * @property {long} time  options.time event timeStamp, optional
     * @property {String} timeZone  options.timeZone event time zone, optional
     * @property {String} appId app id,optional
     */
    static trackUpdate(options?: any): void;
    /**
     * Sending Overwritable Event
     * @param {Object} options event infomations
     *
     * @property {String} eventName options.eventName event name, required
     * @property {Object} properties  options.properties event properties, optional
     * @property {String} eventId  options.eventId event ID, to mark the event, optional
     * @property {long} time  options.time event timeStamp, optional
     * @property {String} timeZone  options.timeZone event time zone, optional
     * @property {String} appId app id,optional
     */
    static trackOverwrite(options?: any): void;
    /**
     * Record the event duration, call this method to start the timing, stop the timing when the target event is uploaded, and add the attribute #duration to the event properties, in seconds.
     * @param {String} eventName event name,required
     * @param {String} appId app id,optional
     */
    static timeEvent(eventName: string, appId: string): void;
    /**
     * Enable the auto tracking function.
     * @param {TDAutoTrackEventType} autoTrackEventType Indicates the type of the automatic collection event to be enabled,required
     * @param {String} appId app id,optional
     */
    static enableAutoTrack(autoTrackEventType: {
        APP_START: number;
        APP_END: number;
        APP_CRASH: number;
        APP_INSTALL: number;
    }, appId: string): void;
    /**
     * Enable the auto tracking function with properties
     * @param {Object} options  autoTrack infomations
     *
     * @property {TDAutoTrackEventType}autoTrackTypes options.autoTrackEventType Indicates the type of the automatic collection event to be enabled,required
     * @property {Object} properties options.properties track event  properties,required
     * @property {String} appId options.appId app id,optional
     */
    static enableAutoTrackWithProperties(options?: any): void;
    /**
     * Sets the user property, replacing the original value with the new value if the property already exists.
     * @param {Object} properties user properties,required
     * @param {String} appId app id,optional
     */
    static userSet(properties: any, appId: string): void;
    /**
     * Sets a single user attribute, ignoring the new attribute value if the attribute already exists.
     * @param {Object} properties user properties,required
     * @param {String} appId app id,optional
     */
    static userSetOnce(properties: any, appId: string): void;
    /**
     * Reset user properties.
     * @param {String} property user property,required
     * @param {String} appId app id,optional
     */
    static userUnset(property: string, appId: string): void;
    /**
     * Only one attribute is set when the user attributes of a numeric type are added.
     * @param {object} properties user properties,required
     * @param {String} appId app id,optional
     */
    static userAdd(properties: object, appId: string): void;
    /**
     * Append a user attribute of the List type.
     * @param {Object} properties user properties,required
     * @param {String} appId app id,optional
     */
    static userAppend(properties: any, appId: string): void;
    /**
     * The element appended to the library needs to be done to remove the processing, remove the support, and then import.
     * @param {Object} properties user properties,required
     * @param {String} appId app id,optional
     */
    static userUniqAppend(properties: any, appId: string): void;
    /**
     * Delete the user attributes, but retain the uploaded event data. This operation is not reversible and should be performed with caution.
     * @param {String} appId app id,optional
     */
    static userDelete(appId: string): void;
    /**
     * Set the public event attribute, which will be included in every event uploaded after that. The public event properties are saved without setting them each time.
     * @param {Object} properties super properties,required
     * @param {String} appId app id,optional
     */
    static setSuperProperties(properties: any, appId: string): void;
    /**
     * Clears a public event attribute.
     * @param {String} property public event attribute key to clear,required
     * @param {String} appId app id,optional
     */
    static unsetSuperProperty(property: string, appId: string): void;
    /**
     * Clear all public event attributes.
     * @param {String} appId app id,optional
     */
    static clearSuperProperties(appId: string): void;
    /**
     * Gets the public event properties that have been set.
     * @param {String} appId app id,optional
     * @returns Public event properties that have been set
     */
    static getSuperProperties(appId: string): Promise<any>;
    /**
     * Set dynamic public properties. Each event uploaded after that will contain a public event attribute.
     * @param {Object} dynamicProperties dynamic public properties,required
     * @param {String} appId app id,optional
     */
    static setDynamicSuperProperties(dynamicProperties: any, appId: string): void;
    /**
     * Gets prefabricated properties for all events.
     * @param {String} appId app id,optional
     * @returns preset properties
     */
    static getPresetProperties(appId: string): Promise<any>;
    /**
     *  Set the account ID. Each setting overrides the previous value. Login events will not be uploaded.
     * @param {String} loginId account id,required
     * @param {String} appId app id,optional
     */
    static login(loginId: string, appId: string): void;
    /**
     * Clearing the account ID will not upload user logout events.
     * @param {String} appId app id,optional
     */
    static logout(appId: string): void;
    /**
     * Set the distinct ID to replace the default UUID distinct ID.
     * @param {String} distinctId distinct id,required
     * @param {String} appId app id,optional
     */
    static setDistinctId(distinctId: string, appId: string): void;
    /**
     * Get a visitor ID: The #distinct_id value in the reported data.
     * @param {String} appId app id,optional
     * @returns distinct id
     */
    static getDistinctId(appId: string): Promise<any>;
    /**
     * Obtain the device ID.
     * @param {String} appId app id,optional
     * @returns device id,optional
     */
    static getDeviceId(appId: string): Promise<any>;
    /**
     * Empty the cache queue. When this function is called, the data in the current cache queue will attempt to be reported.
     * If the report succeeds, local cache data will be deleted.
     * @param {String} appId app id,optional
     */
    static flush(appId?: string): void;
    /**
     * The switch reporting status is suspended and restored.
     * @param {TDTrackStatus} status TDTrackStatus,required
     * @param {String} appId app id,optional
     */
    static setTrackStatus(status: {
        PAUSE: string;
        STOP: string;
        SAVE_ONLY: string;
        NORMAL: string;
    }, appId: string): void;
    /**
     *  Enable three-party data synchronization.
     * @param {Object} options third infomations
     *
     * @property {TDThirdPartyType} types third types,required
     * @property {Object} params extras,optional
     * @property {String} appId app id,optional
     */
    static enableThirdPartySharing(options?: any): void;

    /**
     * h5 event handler. 
     * @param {String} eventData event data,required
     */
    static h5ClickHandler(eventData: string): void;
}
