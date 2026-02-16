package com.freshfarm.dto;

public class ProfileResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String farmName;
    private String location;
    private String farmingType;
    private String farmSize;
    private String irrigation;
    private String mainCrops;
    private String harvestFrequency;
    private Boolean isAvailable;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getFarmName() { return farmName; }
    public void setFarmName(String farmName) { this.farmName = farmName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getFarmingType() { return farmingType; }
    public void setFarmingType(String farmingType) { this.farmingType = farmingType; }

    public String getFarmSize() { return farmSize; }
    public void setFarmSize(String farmSize) { this.farmSize = farmSize; }

    public String getIrrigation() { return irrigation; }
    public void setIrrigation(String irrigation) { this.irrigation = irrigation; }

    public String getMainCrops() { return mainCrops; }
    public void setMainCrops(String mainCrops) { this.mainCrops = mainCrops; }

    public String getHarvestFrequency() { return harvestFrequency; }
    public void setHarvestFrequency(String harvestFrequency) { this.harvestFrequency = harvestFrequency; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
}
