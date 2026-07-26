import { createSlice, createAsyncThunk, type PayloadAction } from "@reduxjs/toolkit";
import { authService } from "../services/authService";
import type { AccesoPermisoModuloDto, LoginRequestDto, LoginResponseDto, UsuarioInfoDto } from "../types/auth.types";

interface AuthState {
	usuario: UsuarioInfoDto | null;
	token: string | null;
	expiracion: string | null;
	menus: AccesoPermisoModuloDto[];
	isAuthenticated: boolean;
	status: "idle" | "loading" | "succeeded" | "failed";
	error: string | null;
}

const storedSession = authService.getStoredSession();

const initialState: AuthState = {
	usuario: storedSession?.usuario ?? null,
	token: storedSession?.token ?? null,
	expiracion: storedSession?.expiracion ?? null,
	menus: storedSession?.menus ?? [],
	isAuthenticated: Boolean(storedSession?.token),
	status: "idle",
	error: null,
};

export const loginThunk = createAsyncThunk<LoginResponseDto, LoginRequestDto, { rejectValue: string }>("authenticate/login", async (credentials, { rejectWithValue }) => {
	try {
		return await authService.login(credentials);
	} catch (err) {
		return rejectWithValue(err instanceof Error ? err.message : "Error al iniciar sesión");
	}
});

const authSlice = createSlice({
	name: "auth",
	initialState,
	reducers: {
		logout(state) {
			authService.logout();
			state.usuario = null;
			state.token = null;
			state.expiracion = null;
			state.menus = [];
			state.isAuthenticated = false;
		},
		clearError(state) {
			state.error = null;
		},
	},
	extraReducers: builder => {
		builder
			.addCase(loginThunk.pending, state => {
				state.status = "loading";
				state.error = null;
			})
			.addCase(loginThunk.fulfilled, (state, action: PayloadAction<LoginResponseDto>) => {
				state.status = "succeeded";
				state.usuario = action.payload.usuario;
				state.token = action.payload.token;
				state.expiracion = action.payload.expiracion;
				state.menus = action.payload.menus;
				state.isAuthenticated = true;
			})
			.addCase(loginThunk.rejected, (state, action) => {
				state.status = "failed";
				state.error = action.payload ?? "Error al iniciar sesión";
			});
	},
});

export const { logout, clearError } = authSlice.actions;
export default authSlice.reducer;
