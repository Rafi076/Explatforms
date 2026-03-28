module.exports = {
  devServer: {
    port: 8081, // your Vue frontend port
    proxy: {
      '/employees': {
        target: 'http://localhost:8080', // your Spring Boot backend
        changeOrigin: true,
        secure: false
      }
    }
  }
};