import {
	type LucideIcon,
	CircleDot,
	LayoutDashboard,
	ClipboardList,
	FileSpreadsheet,
	PackageCheck,
	Truck,
	Users,
	Settings,
	FileText,
	Bell,
	User,
	Building2,
	MapPin,
	BarChart3,
	Wallet,
	ShieldCheck,
	Boxes,
	Warehouse,
	Briefcase,
	Folder,
	Route as RouteIcon,
} from "lucide-react";

/**
 * El backend entrega el ícono de cada módulo como un string (campo `icono`
 * de AccesoPermisoModuloDto). En la práctica hemos visto dos convenciones
 * distintas según el módulo/versión del backend:
 *  - PascalCase estilo Lucide: "LayoutDashboard", "Truck", "Users"
 *  - minúsculas estilo Material Icons: "settings", "work", "person"
 *
 * Para no depender de una convención única, la búsqueda es case-insensitive
 * (todo se compara en minúscula) y el mapa incluye alias para los nombres
 * Material más comunes que ya vimos en respuestas reales del backend.
 *
 * En vez de importar el catálogo completo de Lucide (~1500 íconos, rompe el
 * tree-shaking e infla el bundle en cientos de KB), mantenemos un mapa curado.
 * Si aparece un ícono nuevo (de cualquiera de las dos convenciones), basta con
 * importarlo aquí y añadir su entrada/alias en minúscula.
 */
const ICON_MAP: Record<string, LucideIcon> = {
	// Alias estilo Material (nombres reales vistos desde el backend)
	settings: Settings,
	work: Briefcase,
	person: User,
	people: Users,
	folder: Folder,

	// Alias estilo Lucide / PascalCase (se comparan en minúscula igual)
	layoutdashboard: LayoutDashboard,
	dashboard: LayoutDashboard,
	clipboardlist: ClipboardList,
	filespreadsheet: FileSpreadsheet,
	packagecheck: PackageCheck,
	truck: Truck,
	users: Users,
	user: User,
	filetext: FileText,
	bell: Bell,
	building2: Building2,
	mappin: MapPin,
	barchart3: BarChart3,
	wallet: Wallet,
	shieldcheck: ShieldCheck,
	boxes: Boxes,
	warehouse: Warehouse,
	route: RouteIcon,
};

export function resolveIcon(iconName: string | null | undefined): LucideIcon {
	if (!iconName) return CircleDot;
	return ICON_MAP[iconName.toLowerCase()] ?? CircleDot;
}
