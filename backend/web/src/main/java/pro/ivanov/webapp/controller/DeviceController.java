package pro.ivanov.webapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.ivanov.webapp.requestModel.DeviceRequest;
import pro.ivanov.webapp.responseModel.DeviceResponse;
import pro.ivanov.webapp.service.DeviceService;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    private final DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponse>> allDevices() {
        return this.deviceService.getAllByUser();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponse> getDevice(@PathVariable String id) {
        return this.deviceService.getById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<DeviceResponse> createDevice(@RequestBody DeviceRequest request) {
        return this.deviceService.create(request);
    }

    @PostMapping("/activate/{id}")
    public ResponseEntity<DeviceResponse> activateDevice(@PathVariable String id) {
        return this.deviceService.activate(id);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<DeviceResponse> updateDevice(@PathVariable String id, @RequestBody DeviceRequest request) {
        return this.deviceService.update(id, request);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteDevice(@PathVariable String id) {
        return this.deviceService.delete(id);
    }
}