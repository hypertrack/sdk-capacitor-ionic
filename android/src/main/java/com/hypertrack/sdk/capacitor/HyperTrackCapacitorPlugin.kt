package com.hypertrack.sdk.capacitor

import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.annotation.CapacitorPlugin
import com.hypertrack.sdk.*
import com.hypertrack.sdk.android.HyperTrack
import com.hypertrack.sdk.android.Result
import com.hypertrack.sdk.capacitor.common.*
import com.hypertrack.sdk.capacitor.common.Serialization.serializeErrors
import com.hypertrack.sdk.capacitor.common.Serialization.serializeOrders

@CapacitorPlugin(name = "HyperTrackCapacitorPlugin")
class HyperTrackCapacitorPlugin : Plugin() {
    private var locateSubscription: HyperTrack.Cancellable? = null

    init {
        initListeners()
    }

    @PluginMethod
    fun addGeotag(call: PluginCall) {
        invokeSdkMethod(SdkMethod.addGeotag, call).toPluginCall(call)
    }

    @PluginMethod
    fun getAllowMockLocation(call: PluginCall) {
        invokeSdkMethod(SdkMethod.getAllowMockLocation, call).toPluginCall(call)
    }

    @PluginMethod
    fun getDeviceId(call: PluginCall) {
        invokeSdkMethod(SdkMethod.getDeviceID, call).toPluginCall(call)
    }

    @PluginMethod
    fun getErrors(call: PluginCall) {
        invokeSdkMethod(SdkMethod.getErrors, call).toPluginCall(call)
    }

    @PluginMethod
    fun getIsAvailable(call: PluginCall) {
        invokeSdkMethod(SdkMethod.getIsAvailable, call).toPluginCall(call)
    }

    @PluginMethod
    fun getIsTracking(call: PluginCall) {
        invokeSdkMethod(SdkMethod.getIsTracking, call).toPluginCall(call)
    }

    @PluginMethod
    fun getLocation(call: PluginCall) {
        invokeSdkMethod(SdkMethod.getLocation, call).toPluginCall(call)
    }

    @PluginMethod
    fun getMetadata(call: PluginCall) {
        invokeSdkMethod(SdkMethod.getMetadata, call).toPluginCall(call)
    }

    @PluginMethod
    fun getName(call: PluginCall) {
        invokeSdkMethod(SdkMethod.getName, call).toPluginCall(call)
    }

    @PluginMethod
    fun getOrderIsInsideGeofence(call: PluginCall) {
        invokeSdkMethod(SdkMethod.getOrderIsInsideGeofence, call).toPluginCall(call)
    }

    @PluginMethod
    fun getOrders(call: PluginCall) {
        invokeSdkMethod(SdkMethod.getOrders, call).toPluginCall(call)
    }

    @PluginMethod
    fun getWorkerHandle(call: PluginCall) {
        invokeSdkMethod(SdkMethod.getWorkerHandle, call).toPluginCall(call)
    }

    @PluginMethod
    fun setAllowMockLocation(call: PluginCall) {
        invokeSdkMethod(SdkMethod.setAllowMockLocation, call).toPluginCall(call)
    }

    @PluginMethod
    fun setIsAvailable(call: PluginCall) {
        invokeSdkMethod(SdkMethod.setIsAvailable, call).toPluginCall(call)
    }

    @PluginMethod
    fun setIsTracking(call: PluginCall) {
        invokeSdkMethod(SdkMethod.setIsTracking, call).toPluginCall(call)
    }

    @PluginMethod
    fun setMetadata(call: PluginCall) {
        invokeSdkMethod(SdkMethod.setMetadata, call).toPluginCall(call)
    }

    @PluginMethod
    fun setName(call: PluginCall) {
        invokeSdkMethod(SdkMethod.setName, call).toPluginCall(call)
    }

    @PluginMethod
    fun setWorkerHandle(call: PluginCall) {
        invokeSdkMethod(SdkMethod.setWorkerHandle, call).toPluginCall(call)
    }

    @PluginMethod
    fun onSubscribedToErrors(call: PluginCall) {
        sendErrorsEvent(HyperTrack.errors)
    }

    @PluginMethod
    fun onSubscribedToIsAvailable(call: PluginCall) {
        sendIsAvailableEvent(HyperTrack.isAvailable)
    }

    @PluginMethod
    fun onSubscribedToIsTracking(call: PluginCall) {
        sendIsTrackingEvent(HyperTrack.isTracking)
    }

    @PluginMethod
    fun onSubscribedToLocate(call: PluginCall) {
        locateSubscription?.cancel()
        locateSubscription =
            HyperTrack.locate { result ->
                sendLocateEvent(result)
            }
    }

    @PluginMethod
    fun onSubscribedToLocation(call: PluginCall) {
        sendLocationEvent(HyperTrack.location)
    }

    @PluginMethod
    fun onSubscribedToOrders(call: PluginCall) {
        sendOrdersEvent(HyperTrack.orders.values)
    }

    private fun sendErrorsEvent(errors: Set<HyperTrack.Error>) {
        sendEvent(
            EVENT_ERRORS,
            serializeErrorsForCapacitor(serializeErrors(errors)).toJSObject(),
        )
    }

    private fun sendIsAvailableEvent(isAvailable: Boolean) {
        sendEvent(
            EVENT_IS_AVAILABLE,
            Serialization.serializeIsAvailable(isAvailable).toJSObject(),
        )
    }

    private fun sendIsTrackingEvent(isTracking: Boolean) {
        sendEvent(
            EVENT_IS_TRACKING,
            Serialization.serializeIsTracking(isTracking).toJSObject(),
        )
    }

    private fun sendLocationEvent(locationResult: Result<HyperTrack.Location, HyperTrack.LocationError>) {
        sendEvent(
            EVENT_LOCATION,
            Serialization.serializeLocationResult(locationResult).toJSObject(),
        )
    }

    private fun sendLocateEvent(locateResult: Result<HyperTrack.Location, Set<HyperTrack.Error>>) {
        sendEvent(
            EVENT_LOCATE,
            Serialization.serializeLocateResult(locateResult).toJSObject(),
        )
    }

    private fun sendOrdersEvent(orders: Collection<HyperTrack.Order>) {
        sendEvent(
            EVENT_ORDERS,
            serializeOrders(orders).toJSObject(),
        )
    }

    private fun initListeners() {
        HyperTrack.subscribeToErrors {
            sendErrorsEvent(it)
        }

        HyperTrack.subscribeToIsAvailable {
            sendIsAvailableEvent(it)
        }

        HyperTrack.subscribeToIsTracking {
            sendIsTrackingEvent(it)
        }

        HyperTrack.subscribeToLocation {
            sendLocationEvent(it)
        }

        HyperTrack.subscribeToOrders {
            sendOrdersEvent(it.values)
        }
    }

    private fun invokeSdkMethod(
        method: SdkMethod,
        call: PluginCall,
    ): WrapperResult<*> {
        val argsJson = call.data
        return when (method) {
            SdkMethod.addGeotag -> {
                withArgs<Map<String, Any?>, Map<String, Any?>>(argsJson) { args ->
                    HyperTrackSdkWrapper.addGeotag(args)
                }
            }

            SdkMethod.getAllowMockLocation -> {
                HyperTrackSdkWrapper.getAllowMockLocation()
            }

            SdkMethod.getDeviceID -> {
                HyperTrackSdkWrapper.getDeviceId()
            }

            SdkMethod.getDynamicPublishableKey -> {
                throw Error("Not implemented")
            }

            SdkMethod.getErrors -> {
                HyperTrackSdkWrapper
                    .getErrors()
                    .mapSuccess {
                        serializeErrorsForCapacitor(it)
                    }
            }

            SdkMethod.getIsAvailable -> {
                HyperTrackSdkWrapper.getIsAvailable()
            }

            SdkMethod.getIsTracking -> {
                HyperTrackSdkWrapper.getIsTracking()
            }

            SdkMethod.getLocation -> {
                HyperTrackSdkWrapper.getLocation()
            }

            SdkMethod.getMetadata -> {
                HyperTrackSdkWrapper.getMetadata()
            }

            SdkMethod.getName -> {
                HyperTrackSdkWrapper.getName()
            }

            SdkMethod.getOrderIsInsideGeofence -> {
                withArgs<Map<String, Any?>, Map<String, Any?>>(argsJson) { args ->
                    HyperTrackSdkWrapper.getOrderIsInsideGeofence(args)
                }
            }

            SdkMethod.getOrders -> {
                HyperTrackSdkWrapper
                    .getOrders()
            }

            SdkMethod.getWorkerHandle -> {
                HyperTrackSdkWrapper.getWorkerHandle()
            }

            SdkMethod.locate -> {
                throw NotImplementedError("Locate is implemented in different way")
            }

            SdkMethod.setAllowMockLocation -> {
                withArgs<Map<String, Any?>, Unit>(argsJson) { args ->
                    HyperTrackSdkWrapper.setAllowMockLocation(args)
                }
            }

            SdkMethod.setDynamicPublishableKey -> {
                throw Error("Not implemented")
            }

            SdkMethod.setIsAvailable -> {
                withArgs<Map<String, Any?>, Unit>(argsJson) { args ->
                    HyperTrackSdkWrapper.setIsAvailable(args)
                }
            }

            SdkMethod.setIsTracking -> {
                withArgs<Map<String, Any?>, Unit>(argsJson) { args ->
                    HyperTrackSdkWrapper.setIsTracking(args)
                }
            }

            SdkMethod.setMetadata -> {
                withArgs<Map<String, Any?>, Unit>(argsJson) { args ->
                    HyperTrackSdkWrapper.setMetadata(args)
                }
            }

            SdkMethod.setName -> {
                withArgs<Map<String, Any?>, Unit>(argsJson) { args ->
                    HyperTrackSdkWrapper.setName(args)
                }
            }

            SdkMethod.setWorkerHandle -> {
                withArgs<Map<String, Any?>, Unit>(argsJson) { args ->
                    HyperTrackSdkWrapper.setWorkerHandle(args)
                }
            }
        }
    }

    private fun sendEvent(
        eventName: String,
        data: JSObject,
        retainUntilConsumed: Boolean = false,
    ) {
        notifyListeners(eventName, data, retainUntilConsumed)
    }

    private inline fun <reified T, N> withArgs(
        args: JSObject,
        crossinline sdkMethodCall: (T) -> WrapperResult<N>,
    ): WrapperResult<N> =
        when (T::class) {
            Map::class -> {
                sdkMethodCall.invoke(args.toMap() as T)
            }

            else -> {
                Failure(IllegalArgumentException(args.toString()))
            }
        }

    companion object {
        private const val EVENT_ERRORS = "errors"
        private const val EVENT_IS_TRACKING = "isTracking"
        private const val EVENT_IS_AVAILABLE = "isAvailable"
        private const val EVENT_LOCATE = "locate"
        private const val EVENT_LOCATION = "location"
        private const val EVENT_ORDERS = "orders"
    }
}
