* Add config option `showRestrictedMessage` which can be used to disable the message that appears when trying to place a crop in a restricted area.
* Allow configuring `growthReduction` per crop.
* Allow specifying `isCrop` to determine if the block is a crop or not. (This changes the warning message when trying to place)

(The values are optional. `growthReduction` defaults to `-1.0` which grabs the config value instead and `isCrop` defaults to `true`)