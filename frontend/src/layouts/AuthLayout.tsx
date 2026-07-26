import { Outlet } from "react-router-dom";
import { ENV } from "../config/env";

export default function AuthLayout() {
	return (
		<div className="flex min-h-screen flex-col bg-gray-50 sm:flex-row">
			<div className="hidden flex-1 flex-col justify-between bg-primary-900 p-10 text-white sm:flex lg:p-16">
				<div className="flex items-center gap-2">
					<div className="flex flex-col items-center max-w-xs">
						<img src="/images/logo/tms/logo_genesis_tema_oscuro.png" alt="Logo-tms" />
					</div>
				</div>
				<div>
					<h2 className="mb-3 text-3xl font-bold leading-tight">Gestiona tus envíos con total visibilidad</h2>
					<p className="max-w-md text-primary-100">Solicita servicios, revisa cotizaciones, controla tus órdenes y sigue tus viajes en tiempo real desde un solo lugar.</p>
				</div>
				<p className="text-xs text-primary-200">
					© {new Date().getFullYear()} {ENV.APP_NAME}. Todos los derechos reservados.
				</p>
			</div>

			<div className="flex flex-1 items-center justify-center p-6 sm:p-10">
				<div className="w-full max-w-md">
					<div className="mb-8 flex items-center gap-2 sm:hidden">
						<img src="/images/logo/tms/logo_genesis.jpg" alt="Logo-tms" className="shadow-xl rounded-2xl w-auto dark:hidden" />
					</div>
					<Outlet />
				</div>
			</div>
		</div>
	);
}
