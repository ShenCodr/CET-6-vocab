<template>
  <div class="app-container">
    <div class="blob blob-1"></div>
    <div class="blob blob-2"></div>
    <router-view></router-view>
  </div>
</template>

<script setup></script>

<style>
.app-container {
  position: relative;
  min-height: 100vh;
  width: 100%;
}
</style>
