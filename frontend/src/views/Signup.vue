<template>
  <div class="signup">
    <v-form ref="form" v-model="valid" lazy-validation>
      <v-text-field
        v-model="name"
        :rules="nameRules"
        :counter="10"
        label="Name"
        required
      ></v-text-field>
      <v-text-field
        v-model="email"
        :rules="emailRules"
        label="E-mail"
        required
      ></v-text-field>
      <v-btn
        :disabled="!valid"
        @click="submit"
      >
        submit
      </v-btn>
      <v-btn @click="clear">clear</v-btn>
    </v-form>
  </div>
</template>
<script>

export default {
  name: 'Signup',
  components: {
  },
  data () {
    return {
      valid: true,
      name: '',
      nameRules: [
        v => !!v || 'Name is required',
        v => (v && v.length <= 10) || 'Name must be less than 10 characters'
      ],
      email: '',
      emailRules: [
        v => !!v || 'E-mail is required',
        v => /.+@.+/.test(v) || 'E-mail must be valid'
      ],
      checkbox: false      
    }
  },
  methods: {
    submit () {
      if (this.$refs.form.validate()) {
        // Native form submission is not yet supported
        this.$api.post('/api/submit', {
          name: this.name,
          email: this.email,
          checkbox: this.checkbox
        })
      }
    },
    clear () {
      this.$refs.form.reset()
    }
  }
}
</script>