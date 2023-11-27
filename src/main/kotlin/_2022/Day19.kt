package _2022

import readInput

fun main() {
    fun parseBlueprint(input: String): BlueprintPaper {
        var number = 0
        val robotBlueprints = mutableListOf<RobotBlueprint>()
        val strings = input.split(".", ":")
        strings.forEachIndexed { index, str ->
            when (index) {
                0 -> number = str.replace("Blueprint ", "")
                    .replace(":", "")
                    .toInt()

                1 -> {
                    val oreRobotNeededOre = str.replace("Each ore robot costs ", "")
                        .replace("ore", "")
                        .trim()
                        .toInt()

                    robotBlueprints.add(RobotBlueprint(Mineral.ORE, oreRobotNeededOre, 0, 0))
                }

                2 -> {
                    val clayRobotOreCost = str.replace("Each clay robot costs ", "")
                        .replace("ore", "")
                        .trim()
                        .toInt()

                    robotBlueprints.add(RobotBlueprint(Mineral.CLAY, clayRobotOreCost, 0, 0))
                }

                3 -> {
                    val numbers = str.replace("Each obsidian robot costs ", "")
                        .replace(" ore and ", ",")
                        .replace(" clay", "")
                        .trim()
                        .split(",")
                        .map(String::toInt)

                    robotBlueprints.add(RobotBlueprint(Mineral.OBSIDIAN, numbers[0], numbers[1], 0))
                }

                4 -> {
                    val numbers = str.replace("Each geode robot costs ", "")
                        .replace(" ore and ", ",")
                        .replace(" obsidian", "")
                        .trim()
                        .split(",")
                        .map(String::toInt)

                    robotBlueprints.add(RobotBlueprint(Mineral.GEODE, numbers[0], 0, numbers[1]))
                }

            }
        }
        return BlueprintPaper(number, robotBlueprints)
    }

    fun canCraftRobots(blueprintPaper: BlueprintPaper, mineralMap: MutableMap<Mineral, Int>): List<RobotBlueprint> {
        return blueprintPaper.robotBlueprints.filter {
            mineralMap.getOrDefault(Mineral.ORE, 0) >= it.oreCost
                    && mineralMap.getOrDefault(Mineral.CLAY, 0) >= it.clayCost
                    && mineralMap.getOrDefault(Mineral.OBSIDIAN, 0) >= it.obsidianCost
        }
    }


    fun filterRobots(
        blueprintPaper: BlueprintPaper,
        existingRobots: MutableList<RobotBlueprint>,
        canCraftRobots: List<RobotBlueprint>,
        mineralMap: MutableMap<Mineral, Int>,
        stepsLeft: Int
    ): List<RobotBlueprint> {

        if (canCraftRobots.isEmpty()) {
            return emptyList()
        }

        val geodeRobots = canCraftRobots.filter { it.miningMineral == Mineral.GEODE }

        if (geodeRobots.isNotEmpty()) {
            return geodeRobots
        }

        val filteredRobots = canCraftRobots.toMutableList()

        val maxNeededOreProduction = blueprintPaper.robotBlueprints.maxOf { it.oreCost }
        val existedOreProduction = existingRobots.count { it.miningMineral == Mineral.ORE }
        if (existedOreProduction >= maxNeededOreProduction) {
            filteredRobots.removeIf { it.miningMineral == Mineral.ORE }
        }
        if (stepsLeft * existedOreProduction + mineralMap.getOrDefault(Mineral.ORE, 0)
            > maxNeededOreProduction * stepsLeft
        ) {
            filteredRobots.removeIf { it.miningMineral == Mineral.ORE }
        }

        val maxNeededClayProduction = blueprintPaper.robotBlueprints.maxOf { it.clayCost }
        val existedClayProduction = existingRobots.count { it.miningMineral == Mineral.CLAY }
        if (existedClayProduction >= maxNeededClayProduction) {
            filteredRobots.removeIf { it.miningMineral == Mineral.CLAY }
        }
        if (stepsLeft * existedClayProduction + mineralMap.getOrDefault(Mineral.CLAY, 0)
            > maxNeededClayProduction * stepsLeft
        ) {
            filteredRobots.removeIf { it.miningMineral == Mineral.CLAY }
        }

        val maxNeededObsidianProduction = blueprintPaper.robotBlueprints.maxOf { it.obsidianCost }
        val existedObsidianProduction = existingRobots.count { it.miningMineral == Mineral.OBSIDIAN }
        if (existedObsidianProduction >= maxNeededObsidianProduction) {
            filteredRobots.removeIf { it.miningMineral == Mineral.OBSIDIAN }
        }

        if (stepsLeft * existedObsidianProduction + mineralMap.getOrDefault(Mineral.OBSIDIAN, 0)
            > maxNeededObsidianProduction * stepsLeft
        ) {
            filteredRobots.removeIf { it.miningMineral == Mineral.OBSIDIAN }
        }

        return filteredRobots
    }

    val hashResults = mutableMapOf<String, Int>()

    fun buildHash(
        mineralMap: MutableMap<Mineral, Int>,
        robots: MutableList<RobotBlueprint>,
        stepsLeft: Int
    ): String {

        val existedOreProduction = robots.count { it.miningMineral == Mineral.ORE }
        val existedClayProduction = robots.count { it.miningMineral == Mineral.CLAY }
        val existedObsidianProduction = robots.count { it.miningMineral == Mineral.OBSIDIAN }
        val existedGeodeProduction = robots.count { it.miningMineral == Mineral.GEODE }

        val clays = mineralMap.getOrDefault(Mineral.CLAY, 0)
        val ores = mineralMap.getOrDefault(Mineral.ORE, 0)
        val obsidians = mineralMap.getOrDefault(Mineral.OBSIDIAN, 0)
        val geodes = mineralMap.getOrDefault(Mineral.GEODE, 0)

        return "step=$stepsLeft" +
                "&obidianrobots=$existedObsidianProduction&clayrobots=$existedClayProduction&orerobots=$existedOreProduction" +
                "&geoderobots=$existedGeodeProduction&clays=$clays&ores=$ores&obsidians=$obsidians&geodes=$geodes"
    }

    var best = -1

    fun dfs(
        blueprintPaper: BlueprintPaper,
        mineralMap: MutableMap<Mineral, Int>,
        robots: MutableList<RobotBlueprint>,
        stepsLeft: Int
    ): Int {

        val stepHash = buildHash(mineralMap, robots, stepsLeft)
        val geodeRobots = robots.count { it.miningMineral == Mineral.GEODE }
        val currentGeodeCount = mineralMap.getOrDefault(Mineral.GEODE, 0)
        if (stepsLeft == 0) {
            best = currentGeodeCount.coerceAtLeast(best)
            hashResults[stepHash] = currentGeodeCount
            return currentGeodeCount
        }

        if (stepsLeft == 1) {
            best = (currentGeodeCount + geodeRobots).coerceAtLeast(best)
            hashResults[stepHash] = currentGeodeCount + geodeRobots
            return currentGeodeCount + geodeRobots
        }

        val possibleMaxGeodes =
            currentGeodeCount + (geodeRobots * stepsLeft) + (0 until stepsLeft).sumOf { geodeRobots + it }
        if (possibleMaxGeodes < best) {
            hashResults[stepHash] = 0
            return 0
        }

        if (hashResults.containsKey(stepHash)) {
            return hashResults[stepHash]!!
        }

        val canCraftRobots = canCraftRobots(blueprintPaper, mineralMap)
        val filteredRobotForCraft = filterRobots(blueprintPaper, robots, canCraftRobots, mineralMap, stepsLeft)

        var maxOfGeodes = 0
        if (filteredRobotForCraft.isNotEmpty()) {
            maxOfGeodes = filteredRobotForCraft.maxOf {
                val nextMineralMap = mineralMap.toMutableMap()

                nextMineralMap[Mineral.CLAY] = nextMineralMap.getOrDefault(Mineral.CLAY, 0) - it.clayCost
                nextMineralMap[Mineral.ORE] = nextMineralMap.getOrDefault(Mineral.ORE, 0) - it.oreCost
                nextMineralMap[Mineral.OBSIDIAN] =
                    nextMineralMap.getOrDefault(Mineral.OBSIDIAN, 0) - it.obsidianCost

                robots.forEach { robot ->
                    nextMineralMap[robot.miningMineral] =
                        nextMineralMap.computeIfAbsent(robot.miningMineral) { _ -> 0 }.inc()
                }

                val nextStepRobots = robots.toMutableList()
                nextStepRobots.add(it)

                dfs(
                    blueprintPaper,
                    nextMineralMap,
                    nextStepRobots,
                    stepsLeft - 1
                )
            }
        }

        var maxWithSkippingBuild = 0
        if (!filteredRobotForCraft.any { it.miningMineral == Mineral.GEODE }) {
            val nextMineralMap = mineralMap.toMutableMap()
            robots.forEach { robot ->
                nextMineralMap[robot.miningMineral] =
                    nextMineralMap.computeIfAbsent(robot.miningMineral) { _ -> 0 }.inc()
            }

            val nextStepRobots = robots.toMutableList()

            maxWithSkippingBuild = dfs(
                blueprintPaper,
                nextMineralMap,
                nextStepRobots,
                stepsLeft - 1
            )
        }


        maxOfGeodes = maxOfGeodes.coerceAtLeast(maxWithSkippingBuild)

        hashResults[stepHash] = maxOfGeodes
        best = maxOfGeodes.coerceAtLeast(best)
        return maxOfGeodes
    }

    fun part1(input: List<String>): Int {
        val bluePrints = input.map(::parseBlueprint)
        return bluePrints.sumOf {
            hashResults.clear()
            best = -1
            val result = dfs(it, mutableMapOf(), mutableListOf(RobotBlueprint(Mineral.ORE, 0, 0, 0)), 24)
            println("$result for ${it.number}")
            result * it.number
        }
    }

    fun part2(input: List<String>): Int {
        val bluePrints = input.map(::parseBlueprint)
        return bluePrints.take(3).map {
            hashResults.clear()
            best = -1
            val result = dfs(it, mutableMapOf(), mutableListOf(RobotBlueprint(Mineral.ORE, 0, 0, 0)), 32)
            println("$result for ${it.number}")
            result
        }.reduce(Int::times)
    }


//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day19_test")
    println(part1(testInput))
    println(part2(testInput))

    val input = readInput("Day19")
    println(part1(input))
    println(part2(input))
}

data class BlueprintPaper(
    val number: Int,
    val robotBlueprints: List<RobotBlueprint>
)

data class RobotBlueprint(
    val miningMineral: Mineral,
    val oreCost: Int,
    val clayCost: Int,
    val obsidianCost: Int
)

enum class Mineral {
    ORE, CLAY, OBSIDIAN, GEODE
}