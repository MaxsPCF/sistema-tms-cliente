import { createSlice } from '@reduxjs/toolkit'

interface UiState {
  sidebarOpen: boolean // menú hamburguesa en mobile
  sidebarCollapsed: boolean // colapso en desktop/tablet
}

const initialState: UiState = {
  sidebarOpen: false,
  sidebarCollapsed: false,
}

const uiSlice = createSlice({
  name: 'ui',
  initialState,
  reducers: {
    toggleMobileSidebar(state) {
      state.sidebarOpen = !state.sidebarOpen
    },
    closeMobileSidebar(state) {
      state.sidebarOpen = false
    },
    toggleSidebarCollapsed(state) {
      state.sidebarCollapsed = !state.sidebarCollapsed
    },
  },
})

export const { toggleMobileSidebar, closeMobileSidebar, toggleSidebarCollapsed } = uiSlice.actions
export default uiSlice.reducer
