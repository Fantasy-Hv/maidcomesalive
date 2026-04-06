cd /home/yian/.local/share/PrismLauncher/instances/1.21.1/minecraft/mods
mkdir tmp_mod
mv ./touhoulittlemaid-1.5.0-neoforge+mc1.21.1-release.jar ./tmp_mod
cd ./tmp_mod
unzip ./touhoulittlemaid-1.5.0-neoforge+mc1.21.1-release.jar
rm ./touhoulittlemaid-1.5.0-neoforge+mc1.21.1-release.jar
cd ./assets/touhou_little_maid/animation
rm ./maid.animation.json
cp /home/yian/Documents/Archive/Code/McMods/maidcomesalive/script/maid.animation.json ./maid.animation.json
cd /home/yian/.local/share/PrismLauncher/instances/1.21.1/minecraft/mods/tmp_mod/
zip -r touhoulittlemaid-1.5.0-neoforge+mc1.21.1-release.jar ./
cp ./touhoulittlemaid-1.5.0-neoforge+mc1.21.1-release.jar ../
cd ..
rm -rf ./tmp_mod