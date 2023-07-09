Forgive my bad English, the following content uses automatic translation software.
The benefit of eating the same food continuously will be reduced. It can also nerf bonemeal.
连续吃同一种食物的收益会降低。同时也可以削弱骨粉。

Config File: hangersystemchange-common.toml
配置文件：

quantityLength (default 20)
After consuming more than a certain amount of food to eliminate the reduction of nutrition(0-MAX_INT)
食用超过多少数量的食物后，才能消除营养价值的降低(0-MAX_INT)

categoryLength (default 10)
More than how many types of food are consumed before the reduction in nutrition is eliminated(0-MAX_INT)
食用超过多少种类的食物后，才能消除营养价值的降低(0-MAX_INT)

decreaseByQuantity (default 0.75)
Nutrition multiplier reduced by quantity limitation(0-1)
因数量限制降低的营养价值乘数(0-1)

decreaseByCategory (default 0.8)
Nutrition multiplier reduced by types limitation(0-1)
因种类限制降低的营养价值乘数(0-1)

whitelist
These foods do not reduce the nutrition, and can refresh the nutrition of other foods(Please make sure the Id is correct)

blacklist
These foods reduce the nutrition and do not refresh the nutrition of other foods(Please make sure the ID is correct)

ignoredFoods
This is food that will be ignored. That is, it does not reduce the nutrition, nor does it refresh the nutrition of other foods(Please make sure the ID is correct)

nerfBonemeal (default: true)
if true, bonemeal only allows the crop to grow at one stage. Set false to disabled.
