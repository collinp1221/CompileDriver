#IMPORTANT: This program WILL NOT WORK unless you change the directory for all assets to your local directory!
# name the installer
OutFile "Compile Driver Installer.exe"

InstallDir $DESKTOP


# default section start; every NSIS script has at least one section.
Section

#Define output path for file
SetOutPath "C:\Program Files\CompileDriver\ai"

#Specify file to download
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\ai\defaultAI.txt"

# default section end
SectionEnd


#Install all TileMaps to the game folder
Section

SetOutPath "C:\Program Files\CompileDriver\TileMaps"

#This will have to be changed to the directory of your game assets
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileMaps\level1.tmx"
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileMaps\level2.tmx"
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileMaps\level3.tmx"
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileMaps\level4.tmx"
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileMaps\level5.tmx"

SectionEnd


#Install all TileSets to the game folder
Section

SetOutPath "C:\Program Files\CompileDriver\TileSets"

#This will have to be changed to the directory of your game assets
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileSets\Cliff_tileset.png"
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileSets\dungeon_tiles.png"
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileSets\dungeon_tiles.tsx"
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileSets\grass-tiles-2-small.png"
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileSets\grass-tiles-2-small.tsx"
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileSets\PathAndObjects.png"
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileSets\PathAndObjects.tsx"
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileSets\tree2-final.png"
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileSets\wood_tileset.png"
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\TileSets\wood_tileset.tsx"

SectionEnd


#Install the .jar file for the game
Section

SetOutPath "C:\Program Files\CompileDriver"

#This will have to be changed to the directory of your game assets
File "C:\Users\Collin\Desktop\NSIS Tests\CompileDriver\Compile Driver.jar"

SectionEnd

#Create a shortcut
Section 
CreateShortcut "$desktop\Compile Driver.lnk" "C:\Program Files\CompileDriver\Compile Driver.jar"
SectionEnd