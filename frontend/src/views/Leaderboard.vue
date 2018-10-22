<template>
  <div class="about">
    <h1>Leaderboard</h1>
    <v-data-table
        :headers="headers"
        :items="leaderboard"
        hide-actions
        class="elevation-1"
      >
      <template slot="items" slot-scope="props">
        <td>{{ props.item.id }}</td>
        <td class="text-xs-right">{{ props.item.name }}</td>
        <td class="text-xs-right">{{ props.item.score }}</td>
      </template>
    </v-data-table>    
  </div>
</template>
<script>

export default {
  name: 'Leaderboard',
  components: {
  },
  data () {
    return {
      headers: [
        {
          text: 'Position',
          align: 'left',
          sortable: false,
          value: 'position'
        },
        { text: 'Name', value: 'name' },
        { text: 'Score', value: 'score' },
      ],
      leaderboard: [
        { id: 1, name: "Superman", score: 532 },
        { id: 2, name: "Batman", score: 421 },
        { id: 3, name: "Catwoman", score: 235 },
        { id: 4, name: "Icaman", score: 221 },
        { id: 5, name: "Red Tornado", score: 193 },
      ]
    }
  }
}
</script>