<script lang="ts">
	import { Badge } from '$lib/components/ui/badge';
	import EnvelopeClosed from 'svelte-radix/EnvelopeClosed.svelte';

	const dateInitialVigency: string = new Date('2023-10-15T12:00:00').toISOString().split('T')[0];
	const supportEmail: string = 'support@ggeorge.xyz';
</script>

<section>
	<h1 class="scroll-m-20 text-4xl font-extrabold tracking-tight lg:text-5xl">
		Politica de privacidad
	</h1>
	<section class="py-4 lg:w-4/6">
		<article>
			<h3>Introducción</h3>

			<p>
				Esta Política de Privacidad describe cómo George Giosue ("yo", "mí") recopila, utiliza y
				comparte la información que usted proporciona al utilizar mi aplicación <b>Fise SMS</b>
				("Aplicación")
			</p>
		</article>
		<article>
			<h3>Información que recopila</h3>

			<ul class="ml-4 mt-2 list-disc">
				<li>
					<b>Información de SMS:</b> Recopilo el contenido de los mensajes de texto que envía y recibe
					del número +55555. Como el monto del cupón, codigo del cupón y el estado de la transacción.
					Así como el DNI del proveedor que procesó el cupón, en caso de que haya sido procesado anteriormente.
				</li>
			</ul>
		</article>
		<article>
			<h3>Cómo utilizo la información</h3>
			<article class="mt-4">
				<h4>Utilizo la información que recopila para:</h4>

				<ul class="ml-4 mt-2 list-disc">
					<li>
						<b>Proporcionar los servicios de la Aplicación:</b> Mostrandole el estado de la transacción
						al procesar el cupón y el saldo de su cuenta directamente en la interfaz
					</li>
				</ul>
			</article>
			<article>
				<h4>Cómo comparto la información</h4>
				<p>
					La aplicacción solo tiene permisos de Lectura y Escritura de SMS, por lo que no comparte
					la información que se recopila con terceros. En sintesis los datos recopilados se
					mantienen en el dispositivo del usuario y no se envian a servidores externos.
				</p>
			</article>
		</article>
		<article>
			<h3>Seguridad</h3>
			<p>Tomo medidas razonables para proteger la información que se recopila.</p>
			<ul class="ml-4 mt-2 list-disc">
				<li>
					Almacenamiento seguro: La información de SMS se almacena en el propio dispositivo del
					usuario.
				</li>
				<li>
					Acceso limitado: La aplicación no se ejecuta en segundo plano y solo se ejecuta cuando el
					usuario la abre.
				</li>
			</ul>
		</article>
		<article>
			<h3>Cambios en la Política de Privacidad</h3>
			<p>
				Puedo actualizar esta Política de Privacidad de vez en cuando. Le notificaré de cualquier
				cambio importante en la Política de Privacidad mediante un correo electrónico o un aviso en
				la Aplicación.
			</p>
		</article>
		<article>
			<h3>Contacto</h3>
			<p>
				Si tiene alguna pregunta sobre esta Política de Privacidad, contácteme a:
				<a
					href="mailto:{supportEmail}"
					class="flex items-center gap-2 font-bold text-green-600 underline"
				>
					{supportEmail}

					<EnvelopeClosed class="h-4 w-4 stroke-green-600" />
				</a>
			</p>
		</article>
		<article>
			<h3>Fecha de vigencia</h3>
			<p>
				Esta Política de Privacidad entra en vigor el <Badge variant="default"
					>{dateInitialVigency}</Badge
				>.
			</p>
		</article>
	</section>
</section>
