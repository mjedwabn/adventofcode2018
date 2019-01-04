package io.github.mjedwabn.adventofcode2018.day15

import spock.lang.Specification

import java.util.stream.Collectors


class Day15Test extends Specification {
    def "connection finder"() {
        given:
        def map =
                '''
                #######
                #.....#
                ###..##
                #.#.#.#
                #######
                '''

        expect:
        connectionExists(map, from, to) == exists

        where:
        from   | to     | exists
        [1, 1] | [1, 1] | true
        [1, 1] | [2, 1] | true
        [1, 1] | [3, 1] | true
        [1, 1] | [3, 3] | true
        [1, 1] | [1, 3] | false
        [1, 1] | [5, 3] | false
    }

    def "shortest connection length finder"() {
        given:
        def map =
                '''
                #######
                #.....#
                #..#..#
                #..#..#
                #.....#
                #######
                '''

        expect:
        shortestConnectionLength(map, from, to) == length

        where:
        from   | to     || length
        [1, 1] | [1, 1] || 0
        [1, 1] | [2, 1] || 1
        [1, 1] | [3, 1] || 2
        [2, 3] | [4, 3] || 4
    }

    def "shortest connection finder"() {
        given:
        def map =
                '''
                #######
                #.....#
                #..#..#
                #..#..#
                #.....#
                #######
                '''

        expect:
        shortestPath(map, from, to) == path

        where:
        from   | to     || path
        [1, 1] | [1, 1] || []
        [1, 1] | [2, 1] || [[2, 1]]
        [1, 1] | [3, 1] || [[2, 1], [3, 1]]
        [2, 3] | [4, 3] || [[2, 4], [3, 4], [4, 4], [4, 3]]
    }

    def "shortest reading order path finder"() {
        given:
        def map =
                '''
                ####
                #..#
                #..#
                ####
                '''

        expect:
        shortestPath(map, from, to) == path

        where:
        from   | to     || path
        [1, 1] | [2, 2] || [[2, 1], [2, 2]]
        [1, 2] | [2, 1] || [[1, 1], [2, 1]]
        [2, 1] | [1, 2] || [[1, 1], [1, 2]]
        [2, 2] | [1, 1] || [[2, 1], [1, 1]]
    }

    def "shortest reading order path finder with walls"() {
        given:
        def map =
                '''
                #######
                #.....#
                #..##.#
                #.....#
                #######
                '''

        expect:
        shortestPath(map, from, to) == path

        where:
        from   | to     || path
        [5, 3] | [2, 1] || [[5, 2], [5, 1], [4, 1], [3, 1], [2, 1]]
    }

    def "shortest move"() {
        given:
        def map =
                '''
        ################################
        ###########.......#.##..########
        ###########...#...#......#######
        #########...#....##.#....#######
        #########.#...G..........#######
        #########.#.....G.G......#######
        #########.#......G.G......######
        #########...#....G.EG.....######
        ########.##....GGE.G.....E##..##
        #####....##......G....E........#
        #####.#..##...G......GE.......##
        #####.#..##G.........E.E...#.###
        ########G.....#####...E.##.#.#.#
        ########.#...#######...........#
        ########....#########......###.#
        ####.###..#G#########.......####
        ####......#.#########.......####
        #.........#.#########......#####
        ####....###.#########......##..#
        ###.....###..#######....##..#..#
        ####....#.....#####.....###....#
        ######..#......G......##########
        ######........GE.....###########
        ####.........GE..#.#############
        ####..#...##.##..#.#############
        ####......#####....#############
        #.....###...####...#############
        ##.....####....#...#############
        ####.########..#...#############
        ####...######.###..#############
        ####..##########################
        ################################
'''
        def day15 = new Day15()
        day15.parseInput(map)
        def creature = day15.getSortedCreatures().get(0)

        when:
        day15.makeTurn(creature)
        //.forEach(this::makeTurn);

        then:
        shortestPath(map, from, to) == path

        where:
        from    | to      || path
        [14, 4] | [18, 8] || [[14, 5], [14, 6], [14, 7], [14, 8], [14, 9], [15, 9], [16, 9], [16, 10], [17, 10], [18, 10], [18, 9], [18, 8]]
    }

    def "play sample games"() {
        given:
        def game = new Day15()

        when:
        game.parseInput(map)
        game.play()

        then:
        game.getOutcome() == outcome

        where:
        map            || outcome
        '''
        #######
        #.G...#
        #...EG#
        #.#.#G#
        #..G#E#
        #.....#
        #######
        ''' || 27730
        '''
        #######
        #G..#E#
        #E#E.E#
        #G.##.#
        #...#E#
        #...E.#
        #######
        ''' || 36334
        '''
        #######
        #E..EG#
        #.#G.E#
        #E.##E#
        #G..#.#
        #..E#.#
        #######
        ''' || 39514
        '''
        #######
        #E.G#.#
        #.#G..#
        #G.#.G#
        #G..#.#
        #...E.#
        #######
        ''' || 27755
        '''
        #######
        #.E...#
        #.#..G#
        #.###.#
        #E#G#G#
        #...#G#
        #######
        ''' || 28944
        '''
        #########
        #G......#
        #.E.#...#
        #..##..G#
        #...##..#
        #...#...#
        #.G...G.#
        #.....G.#
        #########
        ''' || 18740
    }

    def "play part 1 game"() {
        given:
        def game = new Day15()

        when:
        game.parseInput(map)
        game.play()

        then:
        game.getOutcome() == outcome

        where:
        map            || outcome
        '''
        ################################
        ###########...G...#.##..########
        ###########...#..G#..G...#######
        #########.G.#....##.#GG..#######
        #########.#.........G....#######
        #########.#..............#######
        #########.#...............######
        #########.GG#.G...........######
        ########.##...............##..##
        #####.G..##G.......E....G......#
        #####.#..##......E............##
        #####.#..##..........EG....#.###
        ########......#####...E.##.#.#.#
        ########.#...#######......E....#
        ########..G.#########..E...###.#
        ####.###..#.#########.....E.####
        ####....G.#.#########.....E.####
        #.........#G#########......#####
        ####....###G#########......##..#
        ###.....###..#######....##..#..#
        ####....#.....#####.....###....#
        ######..#.G...........##########
        ######...............###########
        ####.....G.......#.#############
        ####..#...##.##..#.#############
        ####......#####E...#############
        #.....###...####E..#############
        ##.....####....#...#############
        ####.########..#...#############
        ####...######.###..#############
        ####..##########################
        ################################
        ''' || 179968
    }

    def "part 2 samples"() {
        given:
        def game = new ElvesAlwaysWinGame(map)

        when:
        game.play()

        then:
        game.getOutcome() == outcome
        game.getElfAttackPower() == elfAttackPower

        where:
        map            || outcome | elfAttackPower
        '''
        #######
        #.G...#
        #...EG#
        #.#.#G#
        #..G#E#
        #.....#
        #######
        ''' || 4988    | 15
        '''
        #######
        #E..EG#
        #.#G.E#
        #E.##E#
        #G..#.#
        #..E#.#
        #######
        ''' || 31284   | 4
        '''
        #######
        #E.G#.#
        #.#G..#
        #G.#.G#
        #G..#.#
        #...E.#
        #######
        ''' || 3478    | 15
        '''
        #######
        #.E...#
        #.#..G#
        #.###.#
        #E#G#G#
        #...#G#
        #######
        ''' || 6474    | 12
        '''
        #########   
        #G......#   
        #.E.#...#
        #..##..G#   
        #...##..#   
        #...#...#   
        #.G...G.#   
        #.....G.#   
        #########
        ''' || 1140    | 34
    }

    def "part 2 input"() {
        given:
        def game = new ElvesAlwaysWinGame(map)

        when:
        game.play()

        then:
        game.getOutcome() == outcome
        game.getElfAttackPower() == elfAttackPower

        where:
        map            || outcome | elfAttackPower
        '''
        ################################
        ###########...G...#.##..########
        ###########...#..G#..G...#######
        #########.G.#....##.#GG..#######
        #########.#.........G....#######
        #########.#..............#######
        #########.#...............######
        #########.GG#.G...........######
        ########.##...............##..##
        #####.G..##G.......E....G......#
        #####.#..##......E............##
        #####.#..##..........EG....#.###
        ########......#####...E.##.#.#.#
        ########.#...#######......E....#
        ########..G.#########..E...###.#
        ####.###..#.#########.....E.####
        ####....G.#.#########.....E.####
        #.........#G#########......#####
        ####....###G#########......##..#
        ###.....###..#######....##..#..#
        ####....#.....#####.....###....#
        ######..#.G...........##########
        ######...............###########
        ####.....G.......#.#############
        ####..#...##.##..#.#############
        ####......#####E...#############
        #.....###...####E..#############
        ##.....####....#...#############
        ####.########..#...#############
        ####...######.###..#############
        ####..##########################
        ################################
        ''' || 42098   | 20
    }

    List<List<Integer>> shortestPath(String map, List<Integer> from, List<Integer> to) {
        def finder = new Day15.ShortestConnectionFinder(parse(map))
        finder.runDijkstra(makeLocation(from))
        finder.findShortestPath(makeLocation(to)).stream()
                .map { loc -> [loc.x, loc.y] }.collect(Collectors.toList())
    }

    Location makeLocation(List<Integer> location) {
        new Location(location[0], location[1])
    }

    int shortestConnectionLength(String map, List<Integer> from, List<Integer> to) {
        def finder = new Day15.ShortestConnectionFinder(parse(map))
        finder.runDijkstra(new Location(from[0], from[1]))
        finder.findShortestPath(new Location(to[0], to[1])).size()
    }

    private boolean connectionExists(String map, List<Integer> from, List<Integer> to) {
        new Day15.ConnectionFinder(parse(map)).exists(new Location(from[0], from[1]), new Location(to[0], to[1]))
    }

    private Day15.GameMap parse(String map) {
        def mapLines = Arrays.asList(map.split("\n")).stream()
                .map { s -> s.trim() }
                .filter { s -> !s.empty }
                .collect(Collectors.toList())
        new Day15().parseMap(mapLines)
    }
}
