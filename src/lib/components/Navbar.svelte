<script lang="ts">
	import ThemeToggle from '$lib/components/ThemeToggle.svelte';
	import * as Menubar from '$lib/components/ui/menubar';
</script>

<nav class="flex items-center justify-between">
	<ul
		class="flex space-x-4 rounded-lg px-2 py-1 ring-1 ring-foreground/15 [&>li]:flex [&>li]:items-center [&>li]:rounded-lg [&>li]:p-2 [&>li]:font-semibold [&>li]:transition-colors [&>li]:duration-200"
	>
		<li class="hover:bg-foreground/10">
			<a href="/">
				Inicio <Menubar.Shortcut class="text-md ml-2 hidden lg:inline">⌘H</Menubar.Shortcut>
			</a>
		</li>
		<li class="hover:bg-foreground/10">
			<a href="/privacy">
				Politica de P.
				<Menubar.Shortcut class="text-md ml-2 hidden lg:inline">⌘P</Menubar.Shortcut>
			</a>
		</li>
		<li class="hover:bg-foreground/10">
			<a href="/faq">
				FAQ <Menubar.Shortcut class="text-md ml-2 hidden lg:inline">⌘A</Menubar.Shortcut>
			</a>
		</li>
	</ul>
	<ThemeToggle />
</nav>
