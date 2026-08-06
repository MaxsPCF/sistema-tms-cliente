import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { Mail, Lock } from "lucide-react";
import { toast } from "sonner";
import Input from "../../components/atoms/Input";
import Button from "../../components/atoms/Button";
import { useAuth } from "../../hooks/useAuth";

const schema = z.object({
	usuario: z.string().min(1, "El correo es obligatorio").email("Ingresa un correo válido"),
	password: z.string().min(6, "La contraseña debe tener al menos 6 caracteres"),
});

type LoginFormValues = z.infer<typeof schema>;

interface LocationState {
	from?: { pathname: string };
}

export default function LoginPage() {
	const { login, isLoading } = useAuth();
	const navigate = useNavigate();
	const location = useLocation();

	const {
		register,
		handleSubmit,
		formState: { errors },
	} = useForm<LoginFormValues>({ resolver: zodResolver(schema) });

	async function onSubmit(values: LoginFormValues) {
		const result = await login(values);
		console.log("login", result);
		if (result.meta.requestStatus === "fulfilled") {
			toast.success("Bienvenido de nuevo");
			const state = location.state as LocationState | null;
			const redirectTo = state?.from?.pathname || "/dashboard";
			navigate(redirectTo, { replace: true });
		} else {
			toast.error((result.payload as string) || "No se pudo iniciar sesión");
		}
	}

	return (
		<div>
			<h1 className="text-2xl font-bold text-gray-900">Inicia sesión</h1>
			<p className="mt-1.5 text-sm text-gray-500">Ingresa tus credenciales para acceder a tu panel de cliente.</p>

			<form onSubmit={handleSubmit(onSubmit)} className="mt-8 space-y-4">
				<Input label="Correo electrónico" type="email" placeholder="usr@tms-transporte.com" icon={Mail} error={errors.usuario?.message} {...register("usuario")} />
				<Input label="Contraseña" type="password" placeholder="••••••••" icon={Lock} error={errors.password?.message} {...register("password")} />

				<Button type="submit" className="w-full" loading={isLoading}>
					Iniciar sesión
				</Button>
			</form>

			<p className="mt-6 text-center text-sm text-gray-500">
				¿No tienes una cuenta?{" "}
				<Link to="/registro" className="font-medium text-primary-600 hover:text-primary-700">
					Solicita tu registro
				</Link>
			</p>
		</div>
	);
}
