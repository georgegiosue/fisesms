<script lang="ts">
	import * as Alert from '$lib/components/ui/alert/index.js';
	import InfoCircled from 'svelte-radix/InfoCircled.svelte';
</script>

<section>
	<h1 class="scroll-m-20 text-4xl font-extrabold tracking-tight lg:text-5xl">
		Preguntas Frecuentes
	</h1>
	<section class="py-4 lg:w-4/6">
		<ul class="space-y-4">
			<li id="q1">
				<a href="#q1">
					<h3># ¿Para quien esta dirigida la aplicación?</h3>
				</a>

				<p>
					Para proveedores de GPL (Gas Licuado de Petroleo) que cuentan con autorización de <a
						href="https://www.gob.pe/osinergmin"
						class="font-bold text-green-600 underline"
						target="_blank">OSINERGMIN</a
					>
					y
					<a
						href="https://www.gob.pe/38066-empresa-regional-de-servicio-publico-de-electricidad-del-oriente-s-a-que-es-el-fise"
						class="font-bold text-green-600 underline"
						target="_blank">FISE</a
					> para la venta de GLP envasado y procesamiento de cupones de descuento.
				</p>
			</li>
			<li id="q2">
				<a href="#q2">
					<h3># ¿Porque al cerrar la aplicación no se guarda la sesión?</h3>
				</a>

				<p>
					La aplicación se encuentra en desarrollo, y debido a ello aún no está implementada la
					característica para la ejecución en segundo plano; por lo que se recomienda no cerrar la
					aplicación al procesar cupones de descuento.
				</p>

				<Alert.Root class="mt-4">
					<InfoCircled class="h-4 w-4" />
					<Alert.Title>Aclaración</Alert.Title>
					<Alert.Description>
						Los cupones procesados se guardan en la base de datos del dispositivo, y no se perderán
						al cerrar la aplicación.
					</Alert.Description>
				</Alert.Root>
			</li>
		</ul>
	</section>
</section>
