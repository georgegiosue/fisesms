<script lang="ts" type="module">
	import QrDownload from '$lib/components/QrDownload.svelte';
	import * as Alert from '$lib/components/ui/alert/index.js';
	import { Button } from '$lib/components/ui/button';
	import Commit from 'svelte-radix/Commit.svelte';
	import ExclamationTriangle from 'svelte-radix/ExclamationTriangle.svelte';
</script>

<section>
	<div class="flex items-center gap-2">
		<h1 class="scroll-m-20 text-4xl font-extrabold tracking-tight lg:text-5xl">Fise SMS</h1>
		<img src="icon_playstore.png" alt="Fise SMS Icono" width="64" height="64" />
	</div>
	<div class="grid grid-cols-1 gap-4 lg:grid-cols-2">
		<section class="space-y-4">
			<article class="space-y-2">
				<p class="text-justify antialiased">
					Fise SMS es una aplicación de Android que permite a los proveedores de GLP (Gas Licuado de
					Petróleo) <b>procesar cupones de descuento</b>, <b>verificar el saldo de su cuenta</b> y
					consultar los últimos cupones procesados a través del <b>historial de transacciones</b>.
					Realice solicitudes por SMS de forma rápida y sencilla con una interfaz fácil de usar.
				</p>
				<Button href="https://github.com/georgegiosue/fisesms" target="_blank" class="gap-2">
					Código Fuente
					<Commit />
				</Button>
			</article>
			<article>
				<Alert.Root>
					<ExclamationTriangle class="h-4 w-4" />
					<Alert.Title>¡Aviso!</Alert.Title>
					<Alert.Description
						>Esta aplicación no es oficial del FISE ni del gobierno peruano. Es una iniciativa
						independiente para facilitar el acceso a información y servicios del FISE. La
						información no está garantizada, consulte siempre fuentes oficiales del <a
							href="https://www.gob.pe/38066-empresa-regional-de-servicio-publico-de-electricidad-del-oriente-s-a-que-es-el-fise"
							class="font-bold text-green-600 underline"
							target="_blank">FISE</a
						>.</Alert.Description
					>
				</Alert.Root>
			</article>
		</section>
		<section class="flex flex-col items-end">
			<h5 class="w-[80%] text-end">
				Descargue la aplicación desde la tienda de aplicaciones de Google o F-Droid.
			</h5>
			<div class="flex gap-2">
				<a href="/">
					<img
						src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png"
						alt="Google Play"
						width="160"
						height="160"
					/>
				</a>
				<a href="/">
					<img
						src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"
						alt="F-Droid"
						width="160"
						height="160"
					/>
				</a>
			</div>

			<div class="flex flex-col items-end">
				<p>O escanee el código QR para descargar el APK.</p>
				<QrDownload />
			</div>
			<p class="w-[40%] text-end text-xs text-muted-foreground">
				La aplicación es gratuita y no requiere de conexión a internet para funcionar.
			</p>
		</section>
	</div>
</section>
