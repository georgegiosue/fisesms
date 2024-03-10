<script lang="ts">
	import ThemeToggle from '$lib/components/ThemeToggle.svelte';
	import * as Menubar from '$lib/components/ui/menubar';
	import { ModeWatcher } from 'mode-watcher';
	import { onMount } from 'svelte';
	import '../app.pcss';

	onMount(() => {
		function handleKeydown(e: KeyboardEvent) {
			e.preventDefault();
			const key = e.key;

			const currentHostname = window.location.hostname;
			const currentProtocol = window.location.protocol;
			const currentPort = window.location.port ? `:${window.location.port}` : '';

			switch (key) {
				case 'p':
				case 'P':
					const redirectUrlPrivacy = `${currentProtocol}//${currentHostname}${currentPort}/privacy`;
					window.location.href = redirectUrlPrivacy;
					break;
				case 'a':
				case 'A':
					const redirectUrlFaq = `${currentProtocol}//${currentHostname}${currentPort}/faq`;
					window.location.href = redirectUrlFaq;
					break;

				case 'h':
				case 'H':
					const redirectUrlHome = `${currentProtocol}//${currentHostname}${currentPort}/`;
					window.location.href = redirectUrlHome;
					break;
			}
		}

		document.addEventListener('keydown', handleKeydown);
		return () => {
			document.removeEventListener('keydown', handleKeydown);
		};
	});
</script>

<ModeWatcher />

<svelte:head>
	<title>Fise SMS</title>
	<meta
		name="description"
		content="Aplicación de Android que permite a los proveedores de GLP (Gas Licuado de Petróleo) procesar cupones de descuento"
	/>
</svelte:head>

<main class="m-2 rounded-lg p-4 lg:m-24 lg:mx-48">
	<nav class="flex justify-between">
		<Menubar.Root>
			<Menubar.Menu>
				<Menubar.Trigger>
					<a href="/"> Inicio <Menubar.Shortcut class="text-md ml-2">⌘H</Menubar.Shortcut></a>
				</Menubar.Trigger>
			</Menubar.Menu>
			<Menubar.Menu>
				<Menubar.Trigger>
					<a href="/privacy">
						Politica de Privacidad
						<Menubar.Shortcut class="text-md ml-2">⌘P</Menubar.Shortcut>
					</a>
				</Menubar.Trigger>
			</Menubar.Menu>
			<Menubar.Menu>
				<Menubar.Trigger>
					<a href="/faq"> FAQ <Menubar.Shortcut class="text-md ml-2">⌘A</Menubar.Shortcut></a>
				</Menubar.Trigger>
			</Menubar.Menu>
		</Menubar.Root>

		<ThemeToggle />
	</nav>
	<section class="py-4">
		<slot />
	</section>
</main>
